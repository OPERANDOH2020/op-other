# generic config
root_url = "http://localhost"
timeout = 20
# create account config
create_user_url = root_url + ":8135/operando/interfaces/aapi/aapi/user/register"
# create preferences config
get_osp_policies_url = root_url + ":8096/operando/core/pdb/OSP/?filter="
create_user_privacy_policy_url = root_url + ":8096/operando/core/pdb/user_privacy_policy/"
osp_id = "Ami"