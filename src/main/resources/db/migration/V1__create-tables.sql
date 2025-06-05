CREATE TABLE usuarios(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL UNIQUE,
    permissao VARCHAR(20) NOT NULL
);

CREATE TABLE compradores(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL UNIQUE,
    associado_cespol BOOLEAN NOT NULL
);

CREATE TABLE plantas(
    id SERIAL PRIMARY KEY,
    nome_popular VARCHAR(100) NOT NULL,
    nome_cientifico VARCHAR(100) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    descricao_manejo TEXT,
    foto_url VARCHAR(255),
    quantidade_mudas INTEGER
);

CREATE TABLE lote_mudas(
    id SERIAL PRIMARY KEY,
    planta_id INTEGER NOT NULL,
    data_geracao DATE NOT NULL,
    quantidade INT NOT NULL,

    CONSTRAINT fk_lote_planta FOREIGN KEY (planta_id) REFERENCES plantas(id)
);

CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    comprador_id INTEGER NOT NULL,
    data_entrega DATE NOT NULL,
    quantidade_mudas INTEGER NOT NULL,
    valor NUMERIC(10,2),
    destino VARCHAR(255),
    usuario_id INTEGER NOT NULL,
    planta_id INTEGER NOT NULL,

    CONSTRAINT fk_entrega_comprador FOREIGN KEY (comprador_id) REFERENCES compradores(id),
    CONSTRAINT fk_entrega_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_entrega_planta FOREIGN KEY (planta_id) REFERENCES plantas(id)
);

