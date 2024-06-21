DROP DATABASE IF EXISTS Proyecto;
CREATE DATABASE Proyecto;
USE Proyecto;

CREATE TABLE Usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(75) UNIQUE,
    nickname VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    faccion ENUM('Fuego', 'Hielo', 'Tierra', 'Aire') NOT NULL,
    logeado BOOLEAN NOT NULL DEFAULT FALSE,
    numeroVictoriasUsuario INT DEFAULT 0,
    nivel INT DEFAULT 0
);

CREATE TABLE Personajes (
    id_personaje INT AUTO_INCREMENT,
    nombrePersonaje VARCHAR(20) NOT NULL,
    vida INT NOT NULL,
    estamina INT NOT NULL,
    fuerza INT NOT NULL,
    mana INT NOT NULL,
    usuario_id INT,
    tipo_personaje ENUM('Mago', 'Arquero', 'Guerrero', 'Medico') NOT NULL,
    fuerzaMagica INT,
    PRIMARY KEY (id_personaje, usuario_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(id)
);

CREATE TABLE Partida (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id_1 INT,
    usuario_id_2 INT,
    FOREIGN KEY (usuario_id_1) REFERENCES Usuarios(id),
    FOREIGN KEY (usuario_id_2) REFERENCES Usuarios(id)
);

CREATE TABLE Historial (
    usuario_id1 INT,
    usuario_id2 INT,
    partida_id INT ,
    usuario_ganador INT,
    faccion_ganadora ENUM('Fuego', 'Hielo', 'Tierra', 'Aire') NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id1, usuario_id2, partida_id),
    FOREIGN KEY (usuario_id1) REFERENCES Usuarios(id),
    FOREIGN KEY (usuario_id2) REFERENCES Usuarios(id),
    FOREIGN KEY(partida_id) REFERENCES Partida(id)
);

-- Insertar usuarios
INSERT INTO Usuarios (email, nickname, password, faccion, logeado, numeroVictoriasUsuario, nivel) VALUES 
('ricardodv01@gmail.com', 'RicardoDV', 'BlueSky', 'Fuego', FALSE, 10, 50),
('pablofs@gmail.com', 'PabloFS', 'RedApple', 'Hielo', TRUE, 8, 4),
('lucas@gmail.com', 'LucasG', 'GreenTree', 'Tierra', FALSE, 15, 6),
('ana@gmail.com', 'AnaS', 'WhiteCloud', 'Aire', TRUE, 5, 3),
('maria1@gmail.com', 'MariaC', 'SilverStar', 'Fuego', FALSE, 12, 7),
('pedro@gmail.com', 'PedroM', 'GoldenSun', 'Hielo', TRUE, 3, 2),
('gabriel@gmail.com', 'GabrielV', 'BlackHorse', 'Tierra', FALSE, 18, NULL),
('clara@gmail.com', 'ClaraR', 'PurpleMoon', 'Aire', TRUE, 20, 10),
('juan@gmail.com', 'JuanL', 'OrangeFish', 'Fuego', FALSE, 6, 4),
('pepe@gmail.com', 'PepeG', 'YellowBird', 'Hielo', TRUE, 14, 8);

INSERT INTO Personajes (nombrePersonaje, vida, estamina, fuerza, mana, usuario_id, tipo_personaje, fuerzaMagica) VALUES
('Kratos', 50, 25, 12, 0,  1, 'Guerrero', NULL),
('Merlin', 30, 10, 15, 50,  1, 'Mago', 50),
('Legolas', 40, 40, 10,  0, 1, 'Arquero', NULL),
('DrHouse', 50, 13, 15, 30, 1, 'Medico', 30),
('Aragorn', 45, 35, 20, 0,  2, 'Guerrero', NULL),
('Gandalf', 25, 15, 10, 55, 2, 'Mago', 55),
('RobinHood', 35, 45, 12,  0, 3, 'Arquero', NULL),
('Hawkeye', 50, 40, 15, 0, 3, 'Arquero', NULL),
('Hermione', 30, 20, 10, 60, 4, 'Mago', 60),
('Nightelf', 40, 35, 15,  0, 4, 'Medico', 0),
('Arthur', 55, 50, 25, 0, 5, 'Guerrero', NULL),
('HealerJohn', 50, 20, 10, 40, 5, 'Medico', 40),
('Ranger', 35, 45, 20, 0, 6, 'Arquero', NULL),
('Magician', 20, 10, 5, 65, 6, 'Mago', 65);

-- Insertar algunas partidas entre usuarios
INSERT INTO Partida (usuario_id_1, usuario_id_2) VALUES
(1, 2),
(3, 4),
(5, 6),
(7, 8),
(9, 10);

INSERT INTO Historial (usuario_id1, usuario_id2, partida_id, usuario_ganador, faccion_ganadora) VALUES
(1, 2, 1, 1, 'Fuego'),
(3, 4, 2, 4, 'Aire'),
(5, 6, 3, 5, 'Fuego'),
(7, 8, 4, 8, 'Aire'),
(9, 10, 5, 10, 'Hielo');

DELIMITER $$

CREATE PROCEDURE InsertarPersonaje(
    IN p_nombre VARCHAR(20),
    IN p_vida INT,
    IN p_estamina INT,
    IN p_fuerza INT,
    IN p_mana INT,
    IN p_usuario_id INT,
    IN p_tipo_personaje ENUM('Mago', 'Arquero', 'Guerrero', 'Medico'),
    IN p_fuerza_magica INT
)
BEGIN
    INSERT INTO Personajes (nombrePersonaje, vida, estamina, fuerza, mana, usuario_id, tipo_personaje, fuerzaMagica) 
    VALUES (p_nombre, p_vida, p_estamina, p_fuerza, p_mana, p_usuario_id, p_tipo_personaje, 
            CASE 
                WHEN p_tipo_personaje = 'Mago' OR p_tipo_personaje = 'Medico' THEN p_fuerza_magica
                ELSE NULL
            END);
END $$

DELIMITER ;

DELIMITER $$

DELIMITER $$

CREATE PROCEDURE GetAllPersonajes()
BEGIN
    SELECT 
		id_personaje AS id,
        nombrePersonaje,
        vida,
        estamina,
        fuerza,
        mana,
        tipo_personaje,
        fuerzaMagica
    FROM 
        Personajes
    WHERE 
        tipo_personaje IN ('mago', 'arquero', 'guerrero', 'medico');
END $$

DELIMITER ;


DELIMITER $$
CREATE PROCEDURE GetPersonajesByUsuarioId(IN usuarioId INT)
BEGIN
    SELECT id_personaje, nombrePersonaje, vida, estamina, fuerza, mana, tipo_personaje, fuerzaMagica
    FROM Personajes
    WHERE usuario_id = usuarioId;
END $$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE ActualizarPerfilUsuario(
	IN nuevoId INT,
    IN nuevoNickname VARCHAR(20),
    IN nuevaPassword VARCHAR(20)
)
BEGIN
    UPDATE Usuarios
    SET 
        nickname = p_nickname,
        password = p_password
    WHERE id = p_id;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE IniciarSesionUsuario(
    IN p_email VARCHAR(75),
    IN p_password VARCHAR(20)
)
BEGIN
    DECLARE mensaje VARCHAR(255);
    DECLARE user_count INT DEFAULT 0;
    SELECT COUNT(*) INTO user_count
    FROM Usuarios
    WHERE email = p_email AND password = p_password;
    
    IF user_count = 1 THEN
        SET mensaje = 'Inicio de sesión exitoso';
    ELSE
        SET mensaje = 'Email o contraseña incorrectos';
    END IF;
    SELECT mensaje AS mensaje;
END $$

DELIMITER ;


/*CONSULTA 1*/
SELECT 
p.tipo_personaje,
p.fuerzaMagica
FROM 
Personajes p
WHERE
p.tipo_personaje IN ('Mago', 'Medico') AND
p.fuerzaMagica >= 15
GROUP BY 
p.tipo_personaje,
p.fuerzaMagica;


/*CONSULTA 2*/
SELECT 
p.tipo_personaje,
p.estamina
FROM 
Personajes p
WHERE
p.tipo_personaje = 'Arquero' AND
p.estamina > 30
GROUP BY 
p.tipo_personaje,
p.estamina;


/*CONSULTA 3*/
SELECT 
h.usuario_id1,
u1.nickname AS nickname_1,
h.usuario_id2,
u2.nickname AS nickname_2,
h.partida_id,
h.usuario_ganador,
h.faccion_ganadora,
h.fecha
FROM 
Historial h
JOIN 
Usuarios u1 ON h.usuario_id1 = u1.id
JOIN 
Usuarios u2 ON h.usuario_id2 = u2.id;

/*CONSULTA 4*/

SELECT 
p.id_personaje,
p.nombrePersonaje,
p.tipo_personaje,
p.fuerzaMagica,
p.usuario_id,
pr.id AS partida_id
FROM 
Personajes p
JOIN 
Partida pr ON p.usuario_id = pr.usuario_id_1 OR p.usuario_id = pr.usuario_id_2;

/*CONSULTA 5*/
SELECT 
p.id_personaje,
p.nombrePersonaje,
p.tipo_personaje,
p.fuerzaMagica,
p.usuario_id,
pr.id AS partida_id,
h.usuario_ganador,
h.faccion_ganadora
FROM 
Personajes p
JOIN 
Partida pr ON p.usuario_id = pr.usuario_id_1 OR p.usuario_id = pr.usuario_id_2
JOIN 
Historial h ON pr.id = h.partida_id
WHERE
h.faccion_ganadora = 'Fuego';


/*BORRADO*/
DELETE FROM Usuarios
WHERE logeado = FALSE
AND numeroVictoriasUsuario = 0
AND faccion IS NULL;


/*ACTUALIZACION*/
UPDATE Usuarios
SET numeroVictoriasUsuario = numeroVictoriasUsuario + 1
WHERE id IN (
    SELECT usuario_ganador
    FROM Historial
    GROUP BY usuario_ganador
    HAVING COUNT(*) >= 3
);

UPDATE Personajes
SET fuerzaMagica = nuevo_valor
WHERE usuario_id = 1 AND tipo_personaje = 'Mago';

