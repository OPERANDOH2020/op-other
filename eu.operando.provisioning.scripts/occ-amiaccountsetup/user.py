class User(object):

    def __init__(self, id, password, role):
        self.id = id
        self.password = password
        self.role = role

# a consistent schema for creating passwords
def create_password(id, role):
    return "{0}{1}".format(role,id)
