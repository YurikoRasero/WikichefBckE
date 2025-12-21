-- Create User table
CREATE TABLE IF NOT EXISTS _user (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Create Receta table
CREATE TABLE IF NOT EXISTS _receta (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    descripcion TEXT,
    ingredientes TEXT,
    autor_id INTEGER,
    created_at TIMESTAMP,
    CONSTRAINT fk_receta_autor FOREIGN KEY (autor_id) REFERENCES _user(id)
);

-- Create join table for Receta etiquetas (ElementCollection)
CREATE TABLE IF NOT EXISTS _receta_etiquetas (
    _receta_id INTEGER NOT NULL,
    etiquetas VARCHAR(255),
    CONSTRAINT fk_receta_etiquetas FOREIGN KEY (_receta_id) REFERENCES _receta(id) ON DELETE CASCADE
);

-- Create Comentario table
CREATE TABLE IF NOT EXISTS _comentario (
    id SERIAL PRIMARY KEY,
    texto VARCHAR(1500),
    fecha TIMESTAMP,
    users_id INTEGER,
    receta_id INTEGER,
    CONSTRAINT fk_comentario_user FOREIGN KEY (users_id) REFERENCES _user(id),
    CONSTRAINT fk_comentario_receta FOREIGN KEY (receta_id) REFERENCES _receta(id) ON DELETE CASCADE
);

-- Create Calificacion table
CREATE TABLE IF NOT EXISTS _calificacion (
    id SERIAL PRIMARY KEY,
    puntuacion INTEGER NOT NULL,
    receta_id INTEGER,
    users_id INTEGER,
    CONSTRAINT fk_calificacion_receta FOREIGN KEY (receta_id) REFERENCES _receta(id) ON DELETE CASCADE,
    CONSTRAINT fk_calificacion_user FOREIGN KEY (users_id) REFERENCES _user(id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_receta_autor ON _receta(autor_id);
CREATE INDEX IF NOT EXISTS idx_comentario_user ON _comentario(users_id);
CREATE INDEX IF NOT EXISTS idx_comentario_receta ON _comentario(receta_id);
CREATE INDEX IF NOT EXISTS idx_calificacion_receta ON _calificacion(receta_id);
CREATE INDEX IF NOT EXISTS idx_calificacion_user ON _calificacion(users_id);
CREATE INDEX IF NOT EXISTS idx_user_email ON _user(email);

-- ============================================
-- DML: Insert Dummy Data
-- ============================================

-- Insert dummy users
-- Password is 'password' encrypted with BCrypt (cost 10)
INSERT INTO _user (first_name, last_name, email, password, role) VALUES
('Juan', 'Pérez', 'juan.perez@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER'),
('María', 'García', 'maria.garcia@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER'),
('Carlos', 'López', 'carlos.lopez@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('Ana', 'Martínez', 'ana.martinez@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER')
ON CONFLICT (email) DO NOTHING;

-- Insert dummy recetas
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM _receta LIMIT 1) THEN
        INSERT INTO _receta (titulo, descripcion, ingredientes, autor_id, created_at) VALUES
        ('Pasta Carbonara', 'Deliciosa pasta italiana con panceta y queso parmesano', '[{"nombre":"Espaguetis","cantidad":"400g"},{"nombre":"Panceta","cantidad":"200g"},{"nombre":"Huevos","cantidad":"3u"},{"nombre":"Queso Parmesano","cantidad":"100g"}]', 1, NOW() - INTERVAL '5 days'),
        ('Tacos al Pastor', 'Tacos tradicionales mexicanos con carne marinada', '[{"nombre":"Tortillas","cantidad":"12u"},{"nombre":"Carne de cerdo","cantidad":"500g"},{"nombre":"Piña","cantidad":"1u"},{"nombre":"Cebolla","cantidad":"1u"},{"nombre":"Cilantro","cantidad":"1 manojo"}]', 2, NOW() - INTERVAL '3 days'),
        ('Sushi Roll California', 'Roll de sushi con aguacate, pepino y cangrejo', '[{"nombre":"Arroz para sushi","cantidad":"2 tazas"},{"nombre":"Alga nori","cantidad":"4 hojas"},{"nombre":"Aguacate","cantidad":"2u"},{"nombre":"Pepino","cantidad":"1u"},{"nombre":"Cangrejo","cantidad":"200g"}]', 1, NOW() - INTERVAL '1 day'),
        ('Paella Valenciana', 'Paella tradicional española con arroz, pollo y verduras', '[{"nombre":"Arroz bomba","cantidad":"400g"},{"nombre":"Pollo","cantidad":"500g"},{"nombre":"Judías verdes","cantidad":"200g"},{"nombre":"Azafrán","cantidad":"1 pizca"},{"nombre":"Tomate","cantidad":"2u"}]', 3, NOW() - INTERVAL '7 days'),
        ('Brownies de Chocolate', 'Brownies húmedos y deliciosos con chocolate negro', '[{"nombre":"Chocolate negro","cantidad":"200g"},{"nombre":"Mantequilla","cantidad":"150g"},{"nombre":"Huevos","cantidad":"3u"},{"nombre":"Azúcar","cantidad":"200g"},{"nombre":"Harina","cantidad":"100g"}]', 4, NOW() - INTERVAL '2 days');
    END IF;
END $$;

-- Insert etiquetas for recetas (only if table is empty)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM _receta_etiquetas LIMIT 1) THEN
        INSERT INTO _receta_etiquetas (_receta_id, etiquetas) VALUES
        (1, 'Italiana'),
        (1, 'Pasta'),
        (1, 'Fácil'),
        (2, 'Mexicana'),
        (2, 'Tacos'),
        (2, 'Picante'),
        (3, 'Japonesa'),
        (3, 'Sushi'),
        (3, 'Pescado'),
        (4, 'Española'),
        (4, 'Arroz'),
        (4, 'Tradicional'),
        (5, 'Postre'),
        (5, 'Chocolate'),
        (5, 'Dulce');
    END IF;
END $$;

-- Insert dummy comentarios (only if table is empty)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM _comentario LIMIT 1) THEN
        INSERT INTO _comentario (texto, fecha, users_id, receta_id) VALUES
        ('¡Excelente receta! La hice el fin de semana y quedó perfecta.', NOW() - INTERVAL '4 days', 2, 1),
        ('Muy buena, pero le agregué un poco más de queso parmesano.', NOW() - INTERVAL '3 days', 3, 1),
        ('Los mejores tacos que he probado. ¡Gracias por compartir!', NOW() - INTERVAL '2 days', 1, 2),
        ('Deliciosa receta mexicana. La recomiendo totalmente.', NOW() - INTERVAL '1 day', 4, 2),
        ('El sushi quedó increíble. Perfecto para una cena especial.', NOW() - INTERVAL '12 hours', 2, 3),
        ('La paella es mi favorita. Sabe exactamente como en España.', NOW() - INTERVAL '6 days', 1, 4),
        ('Los brownies están deliciosos. Mi familia los adoró.', NOW() - INTERVAL '1 day', 3, 5);
    END IF;
END $$;

-- Insert dummy calificaciones (only if table is empty)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM _calificacion LIMIT 1) THEN
        INSERT INTO _calificacion (puntuacion, receta_id, users_id) VALUES
        (5, 1, 2),
        (4, 1, 3),
        (5, 2, 1),
        (5, 2, 4),
        (5, 3, 2),
        (5, 4, 1),
        (4, 5, 3),
        (5, 5, 2);
    END IF;
END $$;

