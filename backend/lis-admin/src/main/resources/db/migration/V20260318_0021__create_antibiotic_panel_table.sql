-- Antibiotic Panel Table
-- Stores default antibiotic testing panels per organism group
-- Defines which antibiotics should be tested for a given organism group

CREATE TABLE antibiotic_panel (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    panel_name VARCHAR(200) NOT NULL,
    organism_group VARCHAR(100) NOT NULL,
    antibiotic_code VARCHAR(20) NOT NULL,
    test_priority VARCHAR(20) NOT NULL DEFAULT 'PRIMARY',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_antibiotic_panel_branch_id ON antibiotic_panel(branch_id);
CREATE INDEX idx_antibiotic_panel_organism_group ON antibiotic_panel(organism_group);
CREATE INDEX idx_antibiotic_panel_antibiotic_code ON antibiotic_panel(antibiotic_code);
CREATE INDEX idx_antibiotic_panel_test_priority ON antibiotic_panel(test_priority);
CREATE INDEX idx_antibiotic_panel_is_active ON antibiotic_panel(is_active);
CREATE INDEX idx_antibiotic_panel_is_deleted ON antibiotic_panel(is_deleted);
COMMENT ON TABLE antibiotic_panel IS 'Default antibiotic testing panels per organism group. test_priority: PRIMARY (report first), SECONDARY (add if needed), TERTIARY (add only if resistant to primary).';
