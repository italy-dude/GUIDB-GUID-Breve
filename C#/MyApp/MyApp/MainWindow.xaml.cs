using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Numerics;
using System.Text.RegularExpressions;

namespace MyApp
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private static int FIXED_STRING_LENGTH = 32;
        private static char[] ORIGINAL_ALPHABET;
        private static char[] NEW_ALPHABET;

        // initialize alphabets
        static MainWindow()
        {
            //digits upper case 
            StringBuilder originalStringBuilder = new StringBuilder("0123456789abcdef");
            StringBuilder newStringBuilder = new StringBuilder("123456789ABCDEFGHIJKLMNPQRSTUVWXYZ#*");

            // underscore
            //privateBuilder.append('_');
            // lower case
            //newStringBuilder.append("abcdefghijklmnopqrstuvwxyz");
            int offset;

            List<char> originalList = originalStringBuilder.ToString().ToList<Char>();
            //Collections.shuffle(originalList); // shuffle the list

            ORIGINAL_ALPHABET = new char[originalList.Count];
            offset = 0;
            foreach (Char character in originalList)
            {
                ORIGINAL_ALPHABET[offset++] = character;
            }

            List<Char> newList = newStringBuilder.ToString().ToList<Char>();
            //Collections.shuffle(newList); // shuffle the list

            NEW_ALPHABET = new char[newList.Count];
            offset = 0;
            foreach (Char character in newList)
            {
                NEW_ALPHABET[offset++] = character;
            }
        }

        public MainWindow()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Guid randomGuid = Guid.NewGuid();
            String guid = randomGuid.ToString();
            GUIDTextBox.Text = guid;
            guid = guid.Replace("-", "");
            String guidb = encode(guid);
            GUIDBTextBox.Text = guidb;

            for (int i = 0; i < 1; i++)
            {
                String uuids = "ac9f8af9-190e-421d-bf82-56933f8af2e2";//Guid.NewGuid().ToString();
                String clearString = uuids.Replace("-", "");

                //String clearString = uuids.replaceAll("-", "");
                Console.WriteLine(uuids);
                Console.WriteLine(clearString);
                Console.WriteLine(encode(clearString));
                String decoded = decode(encode(clearString));
                Console.WriteLine(decoded);
                decoded = Regex.Replace(decoded, "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
                Console.WriteLine(decoded);
            }

        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        public static String decode(String encodedText)
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
            int checksum = Int32.Parse(encodedText.Substring(0, 1));
            if (getCheckSum(encodedText.Substring(1, encodedText.Length-1)) != checksum)
            {
                return null;
            }
            BigInteger mapped = getTextAsNumber(encodedText.Substring(1, encodedText.Length-1),
                    NEW_ALPHABET);
            //the below was used when we indicated the length of the string with a char
            //this ha sbeen hardwired as 32 being GUIDs always 32 char long
            /*
            final int length = ArrayUtils.indexOf(NEW_ALPHABET, encodedText
                    .charAt(0));
             */
            //aggiungo padding di 0 a sinistra se lunghezza stringa è inferiore a 32
            String paddedString = getNumberAsText(mapped, ORIGINAL_ALPHABET);
            return paddedString.PadLeft(FIXED_STRING_LENGTH, ORIGINAL_ALPHABET[0]);
        }


        public static String encode(String clearText)
        {
            if (!verify(clearText, ORIGINAL_ALPHABET))
            {
                return null;
            }
            BigInteger mapped = getTextAsNumber(clearText, ORIGINAL_ALPHABET);

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

        private static bool verify(String candidate, char[] publicAlphabet)
        {
            bool result;
            if (candidate == null)
            {
                result = false;
            }
            else
            {
                result = true;
                List<Char> aList = candidate.ToString().ToList<Char>();
                foreach (char ch in aList)
                {
                    if (!publicAlphabet.Contains(ch))
                    {
                        result = false;
                        break;
                    }
                }
            }
            return result;
        }

        private static int getCheckSum(String input)
        {
            System.Text.ASCIIEncoding encoding = new System.Text.ASCIIEncoding();
            byte[] data = encoding.GetBytes(input);

            int tmp;
            int res = 0;
            for (int i = 0; i < data.Length; i++)
            {
                tmp = res << 1;
                tmp += 0xff & data[i];
                res = ((tmp & 0xff) + (tmp >> 8)) & 0xff;
            }
            String crcString = "" + res;
            crcString = crcString.Substring(crcString.Length - 1);
            res = Int32.Parse(crcString);
            if (res == 0)
            {
                res = 3; //we cannot have 0's in the GUID-B so 0 is changed with 3
            }
            return res;
        }

        private static String getNumberAsText(BigInteger number,
            char[] alphabet)
        {
            StringBuilder builder = new StringBuilder();
            BigInteger radix = new BigInteger(alphabet.Length);
            BigInteger quotient = number;
            while (quotient.CompareTo(BigInteger.Zero) > 0)
            {
                //prendi numero in ingresso e dividilo per la lunghezza del nuovo alfabeto (38)
                // in tuple[0] va il risultato della divisione
                // in tuple[1] va il resto
                
                //prendi stringa dell'alfabeto corrispondente al resto, questo sarà sempre minore del divisore (38)
                int remainder = (int)BigInteger.Remainder(quotient, radix);
                //log.infoinfo("q=" + quotient + " , r=" + remainder);
                builder.Append(alphabet[remainder]);
                // prosegui finchè quoziente non è 0, dividendo all'infinito alla fine il quoziente deve diventare 0
                quotient = BigInteger.Divide(quotient, radix);
            }
            //inverto con reverse() perchè in getTextAsNumber ero partito dalla fine
            String tobeReversed = builder.ToString();
            StringBuilder newBuilder = new StringBuilder();
            for (int i = tobeReversed.Length - 1; i >= 0; i--)
            {
                newBuilder.Append(tobeReversed[i]);
            }

            String value = newBuilder.ToString();
            //log.info("Decoded string "+value+" from number "+ number);
            return value;
        }


        private static BigInteger getTextAsNumber(String charString, char[] alphabet)
        {

            BigInteger value = BigInteger.Zero;
            char[] chars = charString.ToCharArray();
            int radix = alphabet.Count();
            int wordLength = chars.Count();
            for (int i = 0; i < wordLength; i++)
            {
                //prende carattere in ingresso partendo dalla fine
                char c = chars[wordLength - (i + 1)];

                // ritorna potenza di 16 della posizione corrente (1, 16, 256, 4096 ...)
                // X = (BigInteger.valueOf(radix).pow(i));
                //ritorna indice nell'alfabeto originale del carattere in ingresso
                // Y = BigInteger.valueOf(ArrayUtils.indexOf(alphabet, c)));
                //aggiungi a value che parte da 0 la moltiplicazione di X per Y
                //value = value.add (X * Y)

                BigInteger bi1 = BigInteger.Pow(radix, i);
                BigInteger bi2 = new BigInteger(Array.IndexOf(alphabet, c));
                value = BigInteger.Add(value, BigInteger.Multiply(bi1, bi2));

            }
            //log.info("Decoded value "+value+" from string "+charString);
            return value;
        }

        private void decodeGUIDBButton_Click(object sender, RoutedEventArgs e)
        {
            String guidb = GUIDBTextBox.Text;
            if(guidb != null)
            {
                String guid = decode(guidb);
                guid = Regex.Replace(guid, "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
                GUIDTextBox.Text = guid;
            }
        }

        private void encodeGUIDButton_Click(object sender, RoutedEventArgs e)
        {
            String guid = GUIDTextBox.Text;
            if(guid != null)
            {
                guid = guid.Replace("-", "");
                String guidb = encode(guid);
                GUIDBTextBox.Text = guidb;
            }
        }
    }
}
