#!/usr/bin/python
from json import dumps

POLICY_TITLE = "FoodCoach Service"
POLICY_URL = "FoodCoach"
SCHEMA = "http://operando.odata.eservices4life.org/foodcoach/$metadata"
FIELDS = [
  "Weight",
  "WeightId",
  "Moment",
  "ID",
  "FoodcoachUserId",
  "ScaleRitualId",
  "RegistrationTimestamp",
  "Date",
]
SUBJECTS = [
    {"type": "User", "allow": "*"},
    {"type": "Nutritionist", "allow": "*"},
    {"type": "Researcher", "allow": "Weight,Moment,Date,FoodcoachUserId"},
    {"type": "MarketShareSPA", "allow": ""}
]

policy = {};
policy["policy_text"] = POLICY_TITLE;
policy["policy_url"] = POLICY_URL;
policy["schema"] = SCHEMA;
policy["workflow"] = [];
policy["policies"] = [];

for f in FIELDS:
    for s in SUBJECTS:
        p = {}
        p["subject"] = s["type"]
        if (s["allow"]=="*" or s["allow"].find(f)>=0):
            p["permission"] = "true"
        else:
            p["permission"] = "false"
        p["action"] = "Access"
        p["resource"] = f
        p["attributes"] = [{}]
        policy["policies"].append(p);

json = dumps(policy, indent=2, sort_keys=True);
print json;
with open("policies.json", "w") as f:
    f.writelines(json);
