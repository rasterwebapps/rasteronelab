-- Antibiotic master
CREATE TABLE antibiotic (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    antibiotic_group VARCHAR(50),
    clsi_breakpoint_s DECIMAL(10,4), -- Susceptible breakpoint
    clsi_breakpoint_r DECIMAL(10,4), -- Resistant breakpoint
    clsi_method VARCHAR(50), -- MIC, DISK_DIFFUSION
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_antibiotic_branch_id ON antibiotic(branch_id);
CREATE INDEX idx_antibiotic_code ON antibiotic(code);
CREATE INDEX idx_antibiotic_group ON antibiotic(antibiotic_group);
CREATE INDEX idx_antibiotic_is_active ON antibiotic(is_active);
CREATE INDEX idx_antibiotic_is_deleted ON antibiotic(is_deleted);

-- Microorganism master
CREATE TABLE microorganism (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(20) NOT NULL,
    gram_type VARCHAR(20), -- GRAM_POSITIVE, GRAM_NEGATIVE, ACID_FAST, FUNGUS, OTHER
    organism_type VARCHAR(50), -- BACTERIA, FUNGUS, PARASITE, VIRUS
    clinical_significance VARCHAR(500),
    colony_morphology VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_microorganism_branch_id ON microorganism(branch_id);
CREATE INDEX idx_microorganism_code ON microorganism(code);
CREATE INDEX idx_microorganism_gram_type ON microorganism(gram_type);
CREATE INDEX idx_microorganism_is_active ON microorganism(is_active);
CREATE INDEX idx_microorganism_is_deleted ON microorganism(is_deleted);

-- Antibiotic-Microorganism mapping
CREATE TABLE antibiotic_organism_mapping (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    antibiotic_id UUID NOT NULL REFERENCES antibiotic(id),
    microorganism_id UUID NOT NULL REFERENCES microorganism(id),
    susceptibility VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN', -- SUSCEPTIBLE, INTERMEDIATE, RESISTANT, NATURAL_RESISTANCE, UNKNOWN
    is_default_panel BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_antibiotic_organism UNIQUE(branch_id, antibiotic_id, microorganism_id)
);

CREATE INDEX idx_abx_org_branch_id ON antibiotic_organism_mapping(branch_id);
CREATE INDEX idx_abx_org_antibiotic_id ON antibiotic_organism_mapping(antibiotic_id);
CREATE INDEX idx_abx_org_microorganism_id ON antibiotic_organism_mapping(microorganism_id);
