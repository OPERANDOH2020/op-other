import config
import csv_readers
from csv_readers import DataReader
import requests
import sys
import json
from OperandoException import OperandoException

def create_user_dict(user):
    password = user.password
    role = user.role
    username = user.id

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

def log_create_user_information(response, user):
    password = user.password
    username = user.id
    
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

def create_user(user):

    url = config.create_user_url
    data = create_user_dict(user)
    response = requests.post(url, json=data, timeout=config.timeout)

    log_create_user_information(response, user)

def find_policy_for_osp(policies, osp_id):
    for policy in policies:
        if policy["policy_url"] == osp_id:
            return policy["policies"]

def get_default_user_privacy_policy():
    url = config.get_osp_policies_url
    response = requests.get(url, timeout=config.timeout)
    osp_policies = json.loads(response.text)
    default_user_privacy_policy_for_osp = find_policy_for_osp(osp_policies, config.osp_id)

    if default_user_privacy_policy_for_osp:
        return default_user_privacy_policy_for_osp
    else:
        raise OperandoException("Failed to get default UPPs. Returned osp_policies were" + str(osp_policies))

def create_privacy_policy_dict(username, preference):
    data = {}
    data["user_id"] = username;
    data["user_preferences"] = []
    data["subscribed_osp_policies"] = []
    policy = {}
    policy["osp_id"] = config.osp_id
    policy["access_policies"] = preference
    data["subscribed_osp_policies"].append(policy)
    data["subscribed_osp_settings"] = []
    return data

def log_post_privacy_policy(username, response):
    print response
    if response.status_code:
        print response.status_code
        success = response.status_code == 201
    else:
        success = False
    if success:
        print "succesfully posted policy for user with name {0}".format(username)
    else:
        print "failed to post policy for {0}".format(username)

def post_user_privacy_policy(username, preference):
    url = config.create_user_privacy_policy_url;
    data = create_privacy_policy_dict(username, preference)
    response = requests.post(url, json=data, timeout=config.timeout)
    log_post_privacy_policy(username, response)

### this is the code that runs when you run this file ###
print "Creating user logins"
args = sys.argv
if len(args) > 1:
    # first "arg" is always script name
    input_path = args[1]

    data_reader = DataReader(input_path)
    default_user_privacy_policy = get_default_user_privacy_policy()
    if (default_user_privacy_policy):
        users = data_reader.read_users()
        for user in users:
            create_user(user)
            post_user_privacy_policy(user.id, default_user_privacy_policy)
else:
    print "please supply the path to a csv text file"
print ""