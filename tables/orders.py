import random
import datetime
import csv
from collections import defaultdict

# used to make sure order ID's are not duplicated
used_order_ids = set()

# get menu.csv and adjust the prices based on item name
def load_menu():
    menu = {}
    with open('menu.csv', mode='r') as file:
        reader = csv.reader(file)
        next(reader)  
        for row in reader:
            item_name, price = row
            menu[item_name] = adjust_price(item_name, float(price))
    return menu

# adjust the prices based on item name(will be done in Java for the actual POS)
def adjust_price(item_name, original_price):
    if item_name.startswith('K_'):
        return 6.60
    elif item_name.startswith('N_'):
        return random.choice([8.30, 9.80, 11.30])
    elif item_name.startswith('F_'):
        return 43.75
    elif original_price == 0:
        return round(random.uniform(5, 15), 2) # for A LA CARTE items
    else:
        return original_price

def generate_random_time_stamp(start_date, end_date):
    time_between_dates = end_date - start_date
    days_between_dates = time_between_dates.days

    peak_days = [
        datetime.date(2023, 10, 7),  # Alabama Game
        datetime.date(2024, 8, 21)   # start of semester
    ]
    
    if random.random() < 0.05:  
        random_date = random.choice(peak_days)
    else:
        all_dates = []
        for i in range(days_between_dates + 1):
            all_dates.append(start_date + datetime.timedelta(days=i))
        
        regular_dates = []
        for date in all_dates:
            if date not in peak_days:
                regular_dates.append(date)

        random_date = random.choice(regular_dates)

    # Define time weights
    time_prob = {
        (12, 20): 100,  # Higher probability for 12:20 PM - lunch time after class ends
        (18, 0): 100,   # Higher probability for 6 PM - dinner time after class ends 
        (8, 0): 0.5      # Lower probability for opening time (8 AM)
    }

    # Generate a list of possible times with weights
    times = []
    probs = []
    for h in range(8, 23):
        for m in range(60):
            times.append((h, m))
            probs.append(time_prob.get((h, m), 5))  

    # Select hour and minute based on weights
    hour, minute = random.choices(times, weights=probs, k=1)[0]
    second = random.randint(0, 59)

    return datetime.datetime.combine(random_date, datetime.time(hour, minute, second))


start_date = datetime.datetime(2023, 9, 23)
end_date = datetime.datetime(2024, 9, 23)

date_counts = {}
for i in range(1000):
    timestamp = generate_random_time_stamp(start_date, end_date)
    date_counts[timestamp.date()] = date_counts.get(timestamp.date(), 0) + 1

def generate_order(menu, employee_names, start_date, end_date):
    global used_order_ids
    
    while True:
        order_id = random.randint(1, 1000000)
        if order_id not in used_order_ids:
            used_order_ids.add(order_id)
            break
    
    employee_name = random.choice(employee_names)
    time_stamp = generate_random_time_stamp(start_date, end_date)
    
    num_items = random.choices([1, 2, 3], weights=[0.3, 0.5, 0.2])[0]
    
    total = 0
    order_items = []
    
    # Ensure at least one normal entree is ordered
    main_dishes = []
    for item in menu.keys():
        if item.startswith('N_'):
            main_dishes.append(item)
    
    main_dish = random.choice(main_dishes)
    price = menu[main_dish]
    order_items.append((main_dish, 1, price))
    total += price

    # Add other items 
    for i in range(num_items - 1):
        item = random.choice(list(menu.keys()))
        price = menu[item]
        quantity = 1
        total += price * quantity
        order_items.append((item, quantity, price))
    
    # Add higher probability for a drink order, water or medium fountain drink from combo
    if random.random() < 0.7:
        drinks = []
        for key in menu.keys():
            if key.startswith('M_fountain_drink') or key == 'bottled_water':
                drinks.append(key)
        drink = random.choice(drinks)
        price = menu[drink]
        order_items.append((drink, 1, price))
        total += price

    return order_id, employee_name, total, time_stamp, order_items


def generate_sql_inserts(num_orders):
    menu = load_menu()
    
    start_date = datetime.datetime(2023, 9, 23)  # One year ago
    end_date = datetime.datetime(2024, 9, 23)    # Current date

    orders = []
    total_sales = 0.0
    employee_names = [
    'Harsh Dave', 'Robert Lightfoot', 'Austin Rummels', 'Jack Weltens', 
    'Haresh Raj', 'Jarren Tobias', 'Donald Duck', 'Ronald David', 
    'Jack Snow', 'John Dana', 'Reagan White', 'Daniella Brady'
    ]
    #Use threshold of 1.2 million 
    while total_sales < 1200000 and len(orders) < num_orders:
        order = generate_order(menu, employee_names, start_date, end_date)
        if total_sales + order[2] > 1200000:
            break
        orders.append(order)
        total_sales += order[2]

    orders.sort(key=lambda x: x[3])

    # SQL commands for CREATE TABLE 
    orders_sql = []
    orders_sql.append("CREATE TABLE orders (order_id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, total NUMERIC NOT NULL, time_stamp TIMESTAMP NOT NULL);")
    orders_sql.append("\nINSERT INTO orders (order_id, name, total, time_stamp) VALUES")

    order_details_sql = []
    order_details_sql.append("CREATE TABLE order_details (id SERIAL PRIMARY KEY, order_id INTEGER REFERENCES orders(order_id), product_name VARCHAR(255) NOT NULL, quantity INTEGER NOT NULL, price NUMERIC NOT NULL);")
    order_details_sql.append("\nINSERT INTO order_details (order_id, product_name, quantity, price) VALUES")

    # INSERT INTO orders
    for i, (order_id, name, total, time_stamp, order_items) in enumerate(orders):
        if i == len(orders) - 1:
            orders_sql.append(f"({order_id}, '{name}', {total:.2f}, '{time_stamp}');")
        else:
            orders_sql.append(f"({order_id}, '{name}', {total:.2f}, '{time_stamp}'),")
        
        for j, (item, quantity, price) in enumerate(order_items):
            is_last = (i == len(orders) - 1) and (j == len(order_items) - 1)
            if is_last:
                order_details_sql.append(f"({order_id}, '{item}', {quantity}, {price:.2f});")
            else:
                order_details_sql.append(f"({order_id}, '{item}', {quantity}, {price:.2f}),")

    return orders_sql, order_details_sql

def write_sql_file(sql_inserts, filename):
    with open(filename, 'w') as file:
        for insert in sql_inserts:
            file.write(insert + '\n')

if __name__ == "__main__":
    num_orders = 50000  
    orders_sql, order_details_sql = generate_sql_inserts(num_orders)
    write_sql_file(orders_sql, 'orders.sql')
    write_sql_file(order_details_sql, 'order_details.sql')

# To delete comments in the sql files:
# DROP TABLE orders;
# DROP TABLE order_details;