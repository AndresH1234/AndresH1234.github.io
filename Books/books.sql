CREATE TABLE books (
    isbn INT PRIMARY KEY,
    titulo VARCHAR(255),
    autor VARCHAR(255),
    anioedicion INT
);

INSERT INTO books (isbn, titulo, autor, anioedicion)
VALUES
    (123456, 'Book Title 1', 'Author 1', 2020),
    (789012, 'Book Title 2', 'Author 2', 2019),
    (345678, 'Book Title 3', 'Author 3', 2021),
    (901234, 'Book Title 4', 'Author 4', 2018);