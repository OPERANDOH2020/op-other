import csv
import user
from user import User

class DataReader(object):
    indexkey = "ID"
    typekey = "UserType"
    accountkey = "HasAmiAccount"

    def __init__(self, csv_path):
        self.csv_path = csv_path

    def read_users(self):
        print "reading from: {0}".format(self.csv_path)
        users = []
        with open(self.csv_path, "r") as csv_file:
            reader = csv.DictReader(csv_file)
            for line in reader:
                user_from_line = self.read_user_from_line(line)
                users.append(user_from_line)
        return users

    def read_user_from_line(self, line):
        index = int(line[DataReader.indexkey])
        role = self.read_user_role_from_line(line)
        password = user.create_password(index, role)
        return User(index, password, role)

    def read_user_role_from_line(self, line):
        role = ""
        has_operando_account = line[DataReader.accountkey] == '1'
        if(has_operando_account):
            type = line[DataReader.typekey]
            if(type == "VL"):
                role = "volunteer_linkup"
            elif (type == "AGNS"):
                role = "abingdon_good_neigbour_scheme"
            elif(type == "Volunteer"):
                role = "Volunteer"
        return role
