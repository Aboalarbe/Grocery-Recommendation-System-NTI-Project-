from flask import Flask,jsonify,request
from flaskext.mysql import MySQL

app = Flask(__name__)

mysql = MySQL()

class Database:
    def __init__(self):
        app.config['MYSQL_DATABASE_USER'] = 'USERNAME'
        app.config['MYSQL_DATABASE_PASSWORD'] = 'DB PASSWORD'
        app.config['MYSQL_DATABASE_DB'] = 'username$DB_NAME'
        app.config['MYSQL_DATABASE_HOST'] = 'HOST URL'
        mysql.init_app(app)
        self.conn = mysql.connect()
        self.cursor = self.conn.cursor()

    def list_items(self):
        self.cursor.execute("SELECT itemA FROM rules LIMIT 30")
        result = self.cursor.fetchall()
        data = {"data":result}
        return jsonify(data)

    def get_recommendation_items(self,selected_item,no_items):
        self.cursor.execute("SELECT itemB FROM rules WHERE itemA = '"+selected_item+"' ORDER BY lift DESC LIMIT "+no_items+"")
        result = self.cursor.fetchall()
        data = {"data":result}
        return jsonify(data)

@app.route('/all_items', methods=["GET", "POST"])
def index():
    db = Database()
    result = db.list_items()
    return result

@app.route('/get_recommendation', methods=["GET", "POST"])
def make_recommendation():
    selectedItem = request.form['selectedItem']
    noOfItems = request.form['noOfItems']
    db2 = Database()
    item_result = db2.get_recommendation_items(selectedItem,noOfItems)
    return item_result

if __name__ == '__main__':
	app.run()
