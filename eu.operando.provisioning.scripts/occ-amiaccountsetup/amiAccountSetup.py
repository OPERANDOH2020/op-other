import config
import csv_readers
from csv_readers import DataReader
import requests
import sys

def create_user_dict(password, role, username):
    data = {}
    data["username"] = username
    data["password"] = password
        
    data["optionalAttrs"] = []
    userTypeAttr = {}
    userTypeAttr["attrName"] = "user_type"
    userTypeAttr["attrValue"] = "normal_user"
    data["optionalAttrs"].append(userTypeAttr)            
    
    if(role):
        data["requiredAttrs"] = []
        userTypeAttr = {}
        userTypeAttr["attrName"] = "role"
        userTypeAttr["attrValue"] = role
        data["requiredAttrs"].append(userTypeAttr)
    return data

def log_create_user_information(password, response, username):
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

def create_user(username, password, role):
    url = config.create_user_url
    data = create_user_dict(password, role, username)
    response = requests.post(url, json=data, timeout=config.timeout)

    log_create_user_information(password, response, username)

def log_get_user_privacy_policy_information(response):
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

def get_default_user_privacy_policy():
    url = config.get_default_user_privacy_policy_url
    response = requests.get(url, timeout=config.timeout)
    
    return log_get_user_privacy_policy_information(response)

def post_preference(username, preference):
    return ""

### this is the code that runs when you run this file ###
print "Creating user logins"
args = sys.argv
if len(args) > 1:
    # first "arg" is always script name
    input_path = args[1]

    data_reader = DataReader(input_path)
    preference = get_default_user_privacy_policy()
    if (preference):
        users = data_reader.read_users()
        for user in users:
            create_user(user.id, user.password, user.role)
            post_preference(user.id, preference)
else:
    print "please supply the path to a csv text file"
print ""