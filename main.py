from flask import Flask, jsonify, request

app = Flask(__name__)

users = [
        {'id': 'user1', 'pwd': 'user1', 'name': 'user1', 'email': 'user1@skku.edu.com'},
        {'id': 'skku', 'pwd': 'skku', 'name': 'skku', 'email': 'skku@skku.edu.com'}
]

newsList = {'user1': [], 'skku': []}
newsList['skku'].append({'title': 'exTitle', 'url': 'exUrl'})

@app.route('/users', methods=['GET'])
def get_users():
    userId = request.args.get('userId')
    userPw = request.args.get('userPw')
    for i in users:
        if i['id'] == userId and i['pwd'] == userPw:
            ok = True
            return jsonify(i)
    return jsonify({'error': 'User not found or incorrect password'}), 404

@app.route('/users/newslist', methods=['GET'])
def get_usersNewslist():
    userId = request.args.get('userId')
    if userId in newsList:
        return jsonify(newsList[usreId])
    else:
        return jsonify({'error': 'user not found'}), 404


@app.route('/users/addNews', methods=['POST'])
def add_userNews():
    article_data = request.get_json()
    userId = article_data.get('id')
    title = article_data.get('title')
    url = article_data.get('url')
    newsList[userId].append({'title': title, 'url': url})

    print(userId)
    print(title)

    return jsonify({'res': 'success'}), 200


if __name__ == '__main__':
    app.run(debug=True,port=8080,  host='0.0.0.0')

