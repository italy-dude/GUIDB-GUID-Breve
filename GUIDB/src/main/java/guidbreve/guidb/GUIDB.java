/*
 * 2006-02-24 version 1
 * GUID-Breve or GUID-B 
 *
 * See Readme on GitHUB https://github.com/italy-dude/GUIDB-GUID-Breve
 */
package guidbreve.guidb;

import guidbreve.guidb.logger.Logger;
import java.math.BigInteger;
import java.util.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class GUIDB
{
    private static final Logger log = new Logger(GUIDB.class);
    private static int FIXED_STRING_LENGTH = 32;
    private static final char[] ORIGINAL_ALPHABET;
    private static final char[] NEW_ALPHABET;

    // initialize alphabets
    static
    {
        final StringBuilder originalStringBuilder = new StringBuilder();
        final StringBuilder newStringBuilder = new StringBuilder();

        // digits
        originalStringBuilder.append("0123456789");
        newStringBuilder.append("123456789");

        // upper case
        originalStringBuilder.append("abcdef");
        newStringBuilder.append("ABCDEFGHIJKLMNPQRSTUVWXYZ#*");

        // underscore
        //privateBuilder.append('_');
        // lower case
        //newStringBuilder.append("abcdefghijklmnopqrstuvwxyz");
        int offset;

        final List<Character> originalList = Arrays.asList(// convert Character[] to List<Character>
                ArrayUtils.toObject(// convert char[] to Character[]
                        originalStringBuilder.toString().toCharArray()));
        //Collections.shuffle(originalList); // shuffle the list

        ORIGINAL_ALPHABET = new char[originalList.size()];
        offset = 0;
        for (final Character character : originalList)
        {
            ORIGINAL_ALPHABET[offset++] = character.charValue();
        }

        final List<Character> newList = Arrays.asList(// convert Character[] to List<Character>
                ArrayUtils.toObject(// convert char[] to Character[]
                        newStringBuilder.toString().toCharArray()));
        //Collections.shuffle(newList); // shuffle the list

        NEW_ALPHABET = new char[newList.size()];
        offset = 0;
        for (final Character character : newList)
        {
            NEW_ALPHABET[offset++] = character.charValue();
        }
    }

    private static int getCheckSum(String input)
    {
        final byte[] data = input.getBytes();
        int tmp;
        int res = 0;
        for (int i = 0; i < data.length; i++)
        {
            tmp = res << 1;
            tmp += 0xff & data[i];
            res = ((tmp & 0xff) + (tmp >> 8)) & 0xff;
        }
        String crcString = "" + res;
        crcString = crcString.substring(crcString.length()-1);
        res = Integer.parseInt(crcString);
        if(res==0)
        {
            res = 3; //we cannot have 0's in the GUID-B so 0 is changed with 3
        }
        return res;
    }

    public static String decode(final String encodedText)
    {
        if (!verify(encodedText, NEW_ALPHABET))
        {
            return null;
        }
        //below was used when we needed the first char indicating string length removed
        /*
        final BigInteger mapped = getTextAsNumber(encodedText.substring(1),
                NEW_ALPHABET);
         */
        int checksum = Integer.parseInt(encodedText.substring(0, 1));
        if (getCheckSum(encodedText.substring(1, encodedText.length())) != checksum)
        {
            return null;
        }
        final BigInteger mapped = getTextAsNumber(encodedText.substring(1, encodedText.length()),
                NEW_ALPHABET);
        //the below was used when we indicated the length of the string with a char
        //this ha sbeen hardwired as 32 being GUIDs always 32 char long
        /*
        final int length = ArrayUtils.indexOf(NEW_ALPHABET, encodedText
                .charAt(0));
         */
        //aggiungo padding di 0 a sinistra se lunghezza stringa è inferiore a 32
        return StringUtils.leftPad(getNumberAsText(mapped, ORIGINAL_ALPHABET),
                FIXED_STRING_LENGTH, ORIGINAL_ALPHABET[0]);
    }

    public static String encode(final String clearText)
    {
        if (!verify(clearText, ORIGINAL_ALPHABET))
        {
            return null;
        }
        final BigInteger mapped = getTextAsNumber(clearText, ORIGINAL_ALPHABET);

        // NEW_ALPHABET[clearText.length()] è la lettera nell'alfabeto nuovo corrispondente alla lunghezza stringa in ingresso
        // il NEW_ALPHABET è 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ*#
        // i GUID hanno lunghezza 32 quindi la lettera sarà sempre W
        /*
        return NEW_ALPHABET[clearText.length()]
                + getNumberAsText(mapped, NEW_ALPHABET);
         */
        String encodedString = getNumberAsText(mapped, NEW_ALPHABET);
        return getCheckSum(encodedString) + encodedString;
    }
    
    public static String randomGUIDB()
    {
        return encode(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    private static String getNumberAsText(final BigInteger number,
            final char[] alphabet)
    {
        final StringBuilder builder = new StringBuilder();
        final BigInteger radix = BigInteger.valueOf(alphabet.length);
        BigInteger quotient = number;
        while (quotient.compareTo(BigInteger.ZERO) > 0)
        {
            //prendi numero in ingresso e dividilo per la lunghezza del nuovo alfabeto (38)
            // in tuple[0] va il risultato della divisione
            // in tuple[1] va il resto
            final BigInteger[] tuple = quotient.divideAndRemainder(radix);
            //prendi stringa dell'alfabeto corrispondente al resto, questo sarà sempre minore del divisore (38)
            int remainder = (int) tuple[1].longValue();
            //log.infoinfo("q=" + quotient + " , r=" + remainder);
            builder.append(alphabet[remainder]);
            // prosegui finchè quoziente non è 0, dividendo all'infinito alla fine il quoziente deve diventare 0
            quotient = tuple[0];
        }
        //inverto con reverse() perchè in getTextAsNumber ero partito dalla fine
        final String value = builder.reverse().toString();
        //log.info("Decoded string "+value+" from number "+ number);
        return value;
    }

    private static BigInteger getTextAsNumber(final String charString,
            final char[] alphabet)
    {

        BigInteger value = BigInteger.ZERO;
        final char[] chars = charString.toCharArray();
        final int radix = alphabet.length;
        final int wordLength = chars.length;
        for (int i = 0; i < wordLength; i++)
        {
            //prende carattere in ingresso partendo dalla fine
            final char c = chars[wordLength - (i + 1)];

            // ritorna potenza di 16 della posizione corrente (1, 16, 256, 4096 ...)
            // X = (BigInteger.valueOf(radix).pow(i));
            //ritorna indice nell'alfabeto originale del carattere in ingresso
            // Y = BigInteger.valueOf(ArrayUtils.indexOf(alphabet, c)));
            //aggiungi a value che parte da 0 la moltiplicazione di X per Y
            //value = value.add (X * Y)
            value = value.add(
                    BigInteger.valueOf(radix).pow(i)
                    .multiply(
                            BigInteger.valueOf(ArrayUtils.indexOf(
                                    alphabet, c))));
        }
        //log.info("Decoded value "+value+" from string "+charString);
        return value;
    }

    private static boolean verify(final String candidate,
            final char[] publicAlphabet)
    {
        boolean result;
        if (candidate == null)
        {
            result = false;
        }
        else
        {
            result = true;
            for (final char ch : candidate.toCharArray())
            {
                if (!ArrayUtils.contains(publicAlphabet, ch))
                {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static void main(String args[])
    {

        for (int i = 0; i < 500; i++)
        {
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString();
            

            String clearString = uuids.replaceAll("-", "");

            log.info(""+GUIDB.encode(clearString));
            log.info("");
            //log.info(" {"+uuids+"}");
            //log.info(uuid.toString());
            //log.info(AlphabetHelper.decode(AlphabetHelper.encode(clearString)));
            String out = GUIDB.decode(GUIDB.encode(clearString));
            //log.info(out);
            String outs = out.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
            //log.info(outs);
            //log.info();
            if (!uuid.toString().equals(outs))
            {
                log.info(clearString + " " + out);
                log.info("Error");
                System.exit(-1);
            }
        }
    }
}

