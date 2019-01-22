import sys


class PythonVersion(object):

    @staticmethod
    def get_version():
        return sys.version_info

    @staticmethod
    def is_python2():
        vernum = PythonVersion.get_version()
        if vernum < (3, 0):
            return True
        return False

    @staticmethod
    def is_python3():
        vernum = PythonVersion.get_version()
        if vernum >= (3, 0):
            return True
        return False
