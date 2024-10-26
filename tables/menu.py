import re

def sql_to_csv(sql_file, csv_file):
    with open(sql_file, 'r') as file:
        sql_content = file.read()

    # Find all INSERT INTO statements
    matches = re.findall(r"\('(.*?)', ([0-9\.]+)\)", sql_content)

    with open(csv_file, 'w') as file:
        file.write("product_name,price\n")  # Write the header
        for match in matches:
            file.write(f"{match[0]},{match[1]}\n")  # Write each menu item

# Usage
sql_to_csv('menu.sql', 'menu.csv')