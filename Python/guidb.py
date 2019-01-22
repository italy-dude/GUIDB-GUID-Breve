import logging
import uuid
import sys
from python_version import PythonVersion


class GUIDB(object):

    log = logging.getLogger(__name__)
    FIXED_STRING_LENGTH = 32

    def __init__(self):
        self.ORIGINAL_ALPHABET = []
        self.NEW_ALPHABET = []

        # digits
        original_alphabet = "0123456789"
        new_alphabet = "123456789"

        # upper case
        original_alphabet += "abcdef"
        new_alphabet += "ABCDEFGHIJKLMNPQRSTUVWXYZ#*"

        for character in original_alphabet:
            self.ORIGINAL_ALPHABET.append(character)

        for character in new_alphabet:
            self.NEW_ALPHABET.append(character)

    @staticmethod
    def verify(candidate, alphabet):
        if candidate is None:
            return False
        else:
            for ch in candidate:
                if not ch in alphabet:
                    return False
        return True

    @staticmethod
    def text_as_number(input_string, alphabet):
        value = 0

        radix = len(alphabet)
        input_length = len(input_string)

        for i in range(0, input_length):
            # prende carattere in ingresso partendo dalla fine
            c = input_string[input_length - (i + 1)]

            # ritorna potenza di 16 della posizione corrente (1, 16, 256, 4096 ...)
            # ritorna indice nell'alfabeto originale del carattere in ingresso
            # aggiungi a value che parte da 0 la moltiplicazione di X per Y

            value = value + ((radix ** i) * alphabet.index(c))
        return value

    @staticmethod
    def number_as_text(number, alphabet):
        output_string = []

        radix = len(alphabet)
        quotient = number

        while quotient > 0:
            remainder = quotient % radix
            if PythonVersion.is_python3():
                remainder = int(remainder) # Python 3 division returns floats, we convert back to int
            output_string.append(alphabet[remainder])
            if PythonVersion.is_python3():
                quotient = quotient // radix # in Python 3 we must specify we want integer division
            else:
                quotient = quotient / radix

        output_string.reverse()
        return output_string

    @staticmethod
    def get_checksum(input_string):
        # this library supports both Python 2 and 3
        data = None
        if PythonVersion.is_python2():
            data = [elem.encode("hex") for elem in input_string]
            data = map(lambda x: int(x, 16), data)
        else:
            data = [elem.encode("utf-8").hex() for elem in input_string]
            data = list(map(lambda x: int(x, 16), data))
        tmp = 0
        res = 0
        for i in range(0, len(data)):
            tmp = res << 1
            tmp += 0xff & int(data[i])
            res = ((tmp & 0xff) + (tmp >> 8)) & 0xff

        crc_string = str(res)
        crc_string = crc_string[len(crc_string)-1:]
        res = int(crc_string)
        if res == 0:
            res = 3 # we cannot have 0's in the GUID-B so 0 is changed with 3

        return res

    def encode(self, clear_text):
        if not self.verify(clear_text, self.ORIGINAL_ALPHABET):
            return None

        mapped = self.text_as_number(clear_text, self.ORIGINAL_ALPHABET)
        encoded_string = self.number_as_text(mapped, self.NEW_ALPHABET)
        encoded_string = ''.join(encoded_string)
        return str(self.get_checksum(encoded_string)) + encoded_string

    def decode(self, encoded_text):
        if not self.verify(encoded_text, self.NEW_ALPHABET):
            return None

        checksum = encoded_text[0: 1]

        if not self.get_checksum(encoded_text[1: len(encoded_text)]) == int(checksum):
            return None

        mapped = self.text_as_number(encoded_text[1: len(encoded_text)], self.NEW_ALPHABET)
        decoded_string = self.number_as_text(mapped, self.ORIGINAL_ALPHABET)
        decoded_string = ''.join(decoded_string)
        return decoded_string.rjust(self.FIXED_STRING_LENGTH, self.ORIGINAL_ALPHABET[0])


if __name__ == "__main__":
    guidb = GUIDB()

    for i in range(0, 100):
        random_uuid = uuid.uuid4()
        uuid_dehyphenated = random_uuid.hex
        print(uuid_dehyphenated)

        guidb_str = (guidb.encode(uuid_dehyphenated))
        print(guidb_str)

        uuid_decoded = guidb.decode(guidb_str)
        print(uuid_decoded)

        if not uuid_dehyphenated == uuid_decoded:
            print("Error")
            break
        print("")

    uuidh = guidb.decode("75AWX3URGT1E4242815ZAAGTM9")
    print(uuidh)
