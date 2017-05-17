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
                index = int(line[DataReader.indexkey])
                if(line[DataReader.accountkey]):
                    # only bother giving roles to people who have ami accounts
                    type = line[DataReader.typekey]
                    if(type == "VO"):
                        role = "VolunteerOrganisationAdmin"
                    elif(type == "Volunteer"):
                        role = "Volunteer"
                    else:
                        role = ""
                    password = user.create_password(index, role)
                else:
                    role = ""
                users.append(User(index, password, role))
        return users