-- CLSI Breakpoint Table
-- Stores CLSI M100 antimicrobial susceptibility breakpoints per organism group and antibiotic

CREATE TABLE clsi_breakpoint (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    organism_group VARCHAR(100) NOT NULL,
    antibiotic_code VARCHAR(20) NOT NULL,
    method VARCHAR(30) NOT NULL DEFAULT 'DISC_DIFFUSION',
    sensitive_threshold DECIMAL(8,2),
    intermediate_min DECIMAL(8,2),
    intermediate_max DECIMAL(8,2),
    resistant_threshold DECIMAL(8,2),
    unit VARCHAR(20) NOT NULL DEFAULT 'mm',
    guideline_version VARCHAR(20) DEFAULT 'CLSI M100 2024',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_clsi_breakpoint_branch_id ON clsi_breakpoint(branch_id);
CREATE INDEX idx_clsi_breakpoint_organism_group ON clsi_breakpoint(organism_group);
CREATE INDEX idx_clsi_breakpoint_antibiotic_code ON clsi_breakpoint(antibiotic_code);
CREATE INDEX idx_clsi_breakpoint_method ON clsi_breakpoint(method);
CREATE INDEX idx_clsi_breakpoint_is_active ON clsi_breakpoint(is_active);
CREATE INDEX idx_clsi_breakpoint_is_deleted ON clsi_breakpoint(is_deleted);
COMMENT ON TABLE clsi_breakpoint IS 'CLSI M100 2024 antimicrobial susceptibility breakpoints. Disc diffusion: zone in mm (S >= sensitive_threshold, R <= resistant_threshold). MIC: mcg/mL (S <= sensitive_threshold, R >= resistant_threshold).';
