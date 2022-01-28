CREATE TABLE contact(
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        first_name VARCHAR(10) NOT NULL,
                        phone VARCHAR(10) NOT NULL,
                        address VARCHAR(100) NULL);

CREATE TABLE info(
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     position VARCHAR(10) NOT NULL,
                     company VARCHAR(15) NOT NULL,
                     contact_id INT NOT NULL,
                     FOREIGN KEY (contact_id)
                         REFERENCES contact(id));
