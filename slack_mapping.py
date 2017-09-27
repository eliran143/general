from slacker import Slacker
import redis

slack = Slacker('slack token')

response = slack.users.list()
users = response.body['members']

r = redis.StrictRedis(host='ip', port=6379, db=0)
for i in range(len(users)):
    if users[i]["is_bot"] or users[i]["name"] == "slackbot":
        print("found bot user - skipping")
    else:
        r.set(users[i]["profile"]["email"], users[i]["profile"]["display_name"])
