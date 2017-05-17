import config
import csv_readers
from csv_readers import DataReader
import requests
import sys

def post_user(username, password, role):
    # send user to operando
    url = config.create_user_url
    if(role):
        data = {
            "username" : username,
            "password" : password,
            "optionalAttrs": [
                {
                    "attrName": "user_type",
                    "attrValue": "normal_user"
                }
            ],
            "requiredAttrs": [
                {
                    "attrName": "role",
                    "attrValue": role
                }
            ]
        }
    else:
        data = {
            "username" : username,
            "password" : password,
            "optionalAttrs": [
                {
                    "attrName": "user_type",
                    "attrValue": "normal_user"
                }
            ]
        }
    response = requests.post(url, json=data, timeout=config.timeout)
    print response
    if response.status_code:
        print response.status_code
        success = response.status_code == 201
    else:
        success = False
    if success:
        print "succesfully created user with name {0} and password {1}".format(username, password)
    else:
        print "failed to create user {0}".format(username)

def get_default_preference():
    url = config.get_default_preference_url
    response = requests.get(url, timeout=config.timeout)
    if response.status_code:
        print response.status_code
        success = response.status_code == 200
    else:
        success = False
    if success:
        print "succesfully obtained default preference {0}".format(response.text)
        return response.text
    else:
        print "failed to obtain default preference {0}".format(username)
        return ""

def post_preference(username, preference):
    return ""

### this is the code that runs when you run this file ###
print "Creating user logins"
args = sys.argv
if len(args) > 1:
    # first "arg" is always script name
    input_path = args[1]

    data_reader = DataReader(input_path)
    preference = get_default_preference()
    if (preference):
        users = data_reader.read_users()
        for user in users:
            post_user(user.id, user.password, user.role)
            post_preference(user.id, preference)
else:
    print "please supply the path to a csv text file"
print ""