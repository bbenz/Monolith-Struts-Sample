INSERT INTO roles(id, name) VALUES
  ('r-admin', 'ADMIN'),
  ('r-user', 'USER');

INSERT INTO users(id, email, username, password_hash, salt, status, role, created_at, updated_at) VALUES
  ('u-1', 'user@example.com', 'demo', 'hash', 'salt', 'ACTIVE', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO categories(id, name, parent_id) VALUES
  ('c-1', 'Ski', NULL);

-- Additional categories
INSERT INTO categories(id, name, parent_id) VALUES
  ('c-boots', 'Boots', NULL),
  ('c-wear', 'Wear', NULL),
  ('c-helmet', 'Helmet', NULL),
  ('c-glove', 'Glove', NULL),
  ('c-pole', 'Pole', NULL),
  ('c-wax', 'Wax', NULL);

INSERT INTO products(id, name, brand, description, category_id, sku, status, created_at, updated_at) VALUES
  ('P001', 'Ski A', 'BrandX', 'Entry ski', 'c-1', 'SKU-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample products (20 each per category)
INSERT INTO products(id, name, brand, description, category_id, sku, status, created_at, updated_at) VALUES
  -- Skis (c-1)
  ('PSK001', 'Atomic Redster G9 [Advanced Men]', 'Atomic', 'Advanced men''s for high-speed carving', 'c-1', 'SKI-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK002', 'Head Supershape e-Speed [Advanced Men]', 'Head', 'Advanced on-piste', 'c-1', 'SKI-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK003', 'Salomon S/Max 12 [Advanced Women]', 'Salomon', 'Advanced women''s carving', 'c-1', 'SKI-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK004', 'Rossignol Experience 86 [Intermediate Men]', 'Rossignol', 'Intermediate men''s all-mountain', 'c-1', 'SKI-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK005', 'Rossignol Nova 8 [Intermediate Women]', 'Rossignol', 'Intermediate women''s all-mountain', 'c-1', 'SKI-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK006', 'Fischer RC4 The Curv [Advanced Men]', 'Fischer', 'Advanced men''s racing', 'c-1', 'SKI-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK007', 'Fischer My Curv [Advanced Women]', 'Fischer', 'Advanced women''s racing', 'c-1', 'SKI-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK008', 'K2 Mindbender 90C [Intermediate Women]', 'K2', 'Intermediate women''s freeride', 'c-1', 'SKI-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK009', 'K2 Disruption 78C [Beginner Men]', 'K2', 'Beginner men''s on-piste', 'c-1', 'SKI-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK010', 'Nordica Enforcer 94 [Advanced Men]', 'Nordica', 'Advanced men''s all-mountain', 'c-1', 'SKI-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK011', 'Nordica Santa Ana 88 [Advanced Women]', 'Nordica', 'Advanced women''s all-mountain', 'c-1', 'SKI-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK012', 'Blizzard Brahma 88 [Advanced Men]', 'Blizzard', 'Advanced men''s all-mountain', 'c-1', 'SKI-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK013', 'Blizzard Black Pearl 82 [Intermediate Women]', 'Blizzard', 'Intermediate women''s all-mountain', 'c-1', 'SKI-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK014', 'Volkl Deacon 80 [Intermediate Men]', 'Volkl', 'Intermediate men''s on-piste', 'c-1', 'SKI-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK015', 'Volkl Flair 76 [Beginner Women]', 'Volkl', 'Beginner women''s on-piste', 'c-1', 'SKI-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK016', 'Elan Wingman 82 [Intermediate Men]', 'Elan', 'Intermediate men''s all-mountain', 'c-1', 'SKI-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK017', 'Elan Insomnia 84 [Advanced Women]', 'Elan', 'Advanced women''s on-piste', 'c-1', 'SKI-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK018', 'Dynastar Menace 90 [Beginner Men]', 'Dynastar', 'Beginner men''s park/all-mountain', 'c-1', 'SKI-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK019', 'Atomic Vantage 75 C [Beginner Women]', 'Atomic', 'Beginner women''s all-mountain', 'c-1', 'SKI-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PSK020', 'Salomon QST Spark Jr [Junior]', 'Salomon', 'Junior twin-tip', 'c-1', 'SKI-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Boots (c-boots)
  ('PBT001', 'Salomon S/Pro 100 [Advanced Men]', 'Salomon', 'Advanced men''s all-mountain', 'c-boots', 'BOOT-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT002', 'Salomon S/Pro 90 W [Advanced Women]', 'Salomon', 'Advanced women''s all-mountain', 'c-boots', 'BOOT-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT003', 'Atomic Hawx Prime 100 [Intermediate Men]', 'Atomic', 'Intermediate men''s', 'c-boots', 'BOOT-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT004', 'Atomic Hawx Prime 95 W [Intermediate Women]', 'Atomic', 'Intermediate women''s', 'c-boots', 'BOOT-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT005', 'Lange RX 120 [Advanced Men]', 'Lange', 'Advanced men''s', 'c-boots', 'BOOT-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT006', 'Lange RX 90 W [Intermediate Women]', 'Lange', 'Intermediate women''s', 'c-boots', 'BOOT-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT007', 'Tecnica Mach1 120 [Advanced Men]', 'Tecnica', 'Advanced men''s', 'c-boots', 'BOOT-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT008', 'Tecnica Mach1 95 W [Advanced Women]', 'Tecnica', 'Advanced women''s', 'c-boots', 'BOOT-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT009', 'Nordica Speedmachine 110 [Intermediate Men]', 'Nordica', 'Intermediate men''s', 'c-boots', 'BOOT-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT010', 'Nordica Speedmachine 95 W [Intermediate Women]', 'Nordica', 'Intermediate women''s', 'c-boots', 'BOOT-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT011', 'Dalbello Panterra 100 [Intermediate Men]', 'Dalbello', 'Intermediate men''s', 'c-boots', 'BOOT-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT012', 'Dalbello Panterra 95 W [Intermediate Women]', 'Dalbello', 'Intermediate women''s', 'c-boots', 'BOOT-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT013', 'Head Nexo LYT 110 [Advanced Men]', 'Head', 'Advanced men''s lightweight', 'c-boots', 'BOOT-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT014', 'Head Nexo LYT 100 W [Advanced Women]', 'Head', 'Advanced women''s lightweight', 'c-boots', 'BOOT-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT015', 'Fischer Ranger 110 [Intermediate Men]', 'Fischer', 'Intermediate men''s freeride', 'c-boots', 'BOOT-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT016', 'Fischer My Ranger 90 [Intermediate Women]', 'Fischer', 'Intermediate women''s freeride', 'c-boots', 'BOOT-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT017', 'K2 BFC 100 [Beginner Men]', 'K2', 'Beginner men''s', 'c-boots', 'BOOT-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT018', 'K2 BFC W 90 [Beginner Women]', 'K2', 'Beginner women''s', 'c-boots', 'BOOT-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT019', 'Rossignol Alltrack 90 [Beginner Men]', 'Rossignol', 'Beginner men''s', 'c-boots', 'BOOT-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PBT020', 'Rossignol Alltrack 80 W [Beginner Women]', 'Rossignol', 'Beginner women''s', 'c-boots', 'BOOT-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Wear (c-wear)
  ('PWR001', 'Descente Mizusawa Down Storm [Men]', 'Descente', 'Advanced men''s waterproof down', 'c-wear', 'WEAR-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR002', 'Descente Mizusawa Down Anchor [Women]', 'Descente', 'Advanced women''s waterproof down', 'c-wear', 'WEAR-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR003', 'Goldwin Alpine Jacket G1030 [Men]', 'Goldwin', 'Intermediate men''s jacket', 'c-wear', 'WEAR-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR004', 'Goldwin Alpine Jacket G3030 [Women]', 'Goldwin', 'Intermediate women''s jacket', 'c-wear', 'WEAR-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR005', 'Phenix Norway Alpine Team Jacket [Men]', 'Phenix', 'Advanced men''s team model', 'c-wear', 'WEAR-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR006', 'Phenix Demo Team Jacket [Women]', 'Phenix', 'Advanced women''s demo model', 'c-wear', 'WEAR-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR007', 'The North Face Steep Tech Apogee [Men]', 'The North Face', 'Advanced men''s freeride', 'c-wear', 'WEAR-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR008', 'The North Face Garner Triclimate [Women]', 'The North Face', 'Intermediate women''s 3-in-1', 'c-wear', 'WEAR-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR009', 'Patagonia SnowDrifter Jacket [Men]', 'Patagonia', 'Intermediate men''s backcountry', 'c-wear', 'WEAR-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR010', 'Patagonia Snowbelle Jacket [Women]', 'Patagonia', 'Intermediate women''s all-mountain', 'c-wear', 'WEAR-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR011', 'Burton AK Swash Jacket [Men]', 'Burton', 'Advanced men''s AK', 'c-wear', 'WEAR-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR012', 'Burton AK Embark Jacket [Women]', 'Burton', 'Advanced women''s AK', 'c-wear', 'WEAR-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR013', 'Descente S.I.O INSULATED PANTS [Men]', 'Descente', 'Men''s pants', 'c-wear', 'WEAR-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR014', 'Descente S.I.O INSULATED PANTS W [Women]', 'Descente', 'Women''s pants', 'c-wear', 'WEAR-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR015', 'Goldwin Cordura Pants [Men]', 'Goldwin', 'Men''s pants', 'c-wear', 'WEAR-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR016', 'Goldwin Slim Pants [Women]', 'Goldwin', 'Women''s pants', 'c-wear', 'WEAR-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR017', 'Phenix Demo Team Pants [Men]', 'Phenix', 'Men''s pants', 'c-wear', 'WEAR-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR018', 'Phenix Aurora Pants [Women]', 'Phenix', 'Women''s pants', 'c-wear', 'WEAR-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR019', 'Burton Exile Cargo Pants [Junior]', 'Burton', 'Junior', 'c-wear', 'WEAR-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWR020', 'The North Face Freedom Triclimate [Junior]', 'The North Face', 'Junior', 'c-wear', 'WEAR-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Helmet (c-helmet)
  ('PHL001', 'Giro Range MIPS [Men]', 'Giro', 'Advanced men''s', 'c-helmet', 'HELM-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL002', 'Giro Stellar MIPS [Women]', 'Giro', 'Advanced women''s', 'c-helmet', 'HELM-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL003', 'Smith Vantage MIPS [Men]', 'Smith', 'Advanced men''s', 'c-helmet', 'HELM-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL004', 'Smith Liberty MIPS [Women]', 'Smith', 'Advanced women''s', 'c-helmet', 'HELM-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL005', 'POC Obex SPIN [Men]', 'POC', 'Intermediate men''s', 'c-helmet', 'HELM-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL006', 'POC Obex Pure [Women]', 'POC', 'Intermediate women''s', 'c-helmet', 'HELM-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL007', 'Atomic Revent+ AMID [Men]', 'Atomic', 'Advanced men''s', 'c-helmet', 'HELM-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL008', 'Atomic Revent Q AMID [Women]', 'Atomic', 'Advanced women''s', 'c-helmet', 'HELM-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL009', 'Salomon Pioneer LT [Men]', 'Salomon', 'Intermediate men''s', 'c-helmet', 'HELM-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL010', 'Salomon Icon LT [Women]', 'Salomon', 'Intermediate women''s', 'c-helmet', 'HELM-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL011', 'Scott Symbol 2 Plus [Men]', 'Scott', 'Advanced men''s', 'c-helmet', 'HELM-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL012', 'Scott Keeper 2 JR [Junior]', 'Scott', 'Junior', 'c-helmet', 'HELM-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL013', 'Marker Ampire 2 MIPS [Men]', 'Marker', 'Intermediate men''s', 'c-helmet', 'HELM-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL014', 'Marker Companion W [Women]', 'Marker', 'Intermediate women''s', 'c-helmet', 'HELM-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL015', 'Sweet Protection Switcher [Men]', 'Sweet Protection', 'Advanced men''s', 'c-helmet', 'HELM-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL016', 'Sweet Protection Igniter 2Vi [Men]', 'Sweet Protection', 'Advanced men''s lightweight', 'c-helmet', 'HELM-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL017', 'Giro Crue Youth [Junior]', 'Giro', 'Junior', 'c-helmet', 'HELM-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL018', 'Smith Holt Jr [Junior]', 'Smith', 'Junior', 'c-helmet', 'HELM-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL019', 'Salomon Grom [Junior]', 'Salomon', 'Junior', 'c-helmet', 'HELM-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PHL020', 'POC Pocito Fornix [Junior]', 'POC', 'Junior', 'c-helmet', 'HELM-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Gloves (c-glove)
  ('PGL001', 'Hestra Army Leather Heli Ski [Men]', 'Hestra', 'Advanced men''s', 'c-glove', 'GLOVE-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL002', 'Hestra Army Leather Heli Ski W [Women]', 'Hestra', 'Advanced women''s', 'c-glove', 'GLOVE-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL003', 'Hestra Fall Line 5-Finger [Men]', 'Hestra', 'Advanced men''s', 'c-glove', 'GLOVE-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL004', 'Black Diamond Guide [Men]', 'Black Diamond', 'Advanced men''s', 'c-glove', 'GLOVE-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL005', 'Black Diamond Mercury Mitts [Women]', 'Black Diamond', 'Advanced women''s mitt', 'c-glove', 'GLOVE-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL006', 'Burton Gore-Tex Gloves [Men]', 'Burton', 'Intermediate men''s', 'c-glove', 'GLOVE-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL007', 'Burton Gore-Tex Gloves W [Women]', 'Burton', 'Intermediate women''s', 'c-glove', 'GLOVE-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL008', 'Leki Griffin Pro 3D [Men]', 'Leki', 'Advanced men''s', 'c-glove', 'GLOVE-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL009', 'Dakine Titan Gore-Tex [Men]', 'Dakine', 'Intermediate men''s', 'c-glove', 'GLOVE-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL010', 'Dakine Sequoia Gore-Tex [Women]', 'Dakine', 'Intermediate women''s', 'c-glove', 'GLOVE-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL011', 'Reusch Worldcup Warrior [Men]', 'Reusch', 'Advanced men''s racing', 'c-glove', 'GLOVE-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL012', 'Swany Toaster Mitt [Men]', 'Swany', 'Intermediate men''s mitt', 'c-glove', 'GLOVE-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL013', 'Level Fly Snowboard Gloves [Men]', 'Level', 'Intermediate men''s', 'c-glove', 'GLOVE-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL014', 'Level Bliss Coral W [Women]', 'Level', 'Intermediate women''s', 'c-glove', 'GLOVE-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL015', 'Burton Profile Mitt [Junior]', 'Burton', 'Junior', 'c-glove', 'GLOVE-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL016', 'Hestra Gauntlet CZone Jr [Junior]', 'Hestra', 'Junior', 'c-glove', 'GLOVE-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL017', 'Black Diamond Spark Jr [Junior]', 'Black Diamond', 'Junior', 'c-glove', 'GLOVE-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL018', 'POW Index Trigger [Men]', 'POW', 'Intermediate men''s', 'c-glove', 'GLOVE-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL019', 'POW Stealth GTX [Women]', 'POW', 'Intermediate women''s', 'c-glove', 'GLOVE-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PGL020', 'Hestra Wakayama [Unisex]', 'Hestra', 'Intermediate unisex', 'c-glove', 'GLOVE-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Poles (c-pole)
  ('PPL001', 'Leki Carbon 14 3D [Men]', 'Leki', 'Advanced men''s lightweight carbon', 'c-pole', 'POLE-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL002', 'Leki Legacy Lite S [Women]', 'Leki', 'Intermediate women''s', 'c-pole', 'POLE-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL003', 'Komperdell Nationalteam Carbon [Men]', 'Komperdell', 'Advanced men''s racing', 'c-pole', 'POLE-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL004', 'Black Diamond Razor Carbon [Men]', 'Black Diamond', 'Advanced men''s backcountry', 'c-pole', 'POLE-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL005', 'Salomon MTN Carbon S3 [Men]', 'Salomon', 'Advanced men''s backcountry', 'c-pole', 'POLE-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL006', 'Scott Team Issue SRS [Men]', 'Scott', 'Advanced men''s', 'c-pole', 'POLE-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL007', 'Atomic AMT SQS W [Women]', 'Atomic', 'Intermediate women''s', 'c-pole', 'POLE-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL008', 'Rossignol Hero GS [Men]', 'Rossignol', 'Advanced men''s racing', 'c-pole', 'POLE-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL009', 'K2 Power Composite [Men]', 'K2', 'Intermediate men''s', 'c-pole', 'POLE-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL010', 'Fischer BCX Variolite [Men]', 'Fischer', 'Intermediate men''s BC', 'c-pole', 'POLE-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL011', 'Gabel Carbon Force WI [Men]', 'Gabel', 'Advanced men''s', 'c-pole', 'POLE-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL012', 'Swix Quantum 6 [Men]', 'Swix', 'Intermediate men''s', 'c-pole', 'POLE-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL013', 'Komperdell Fatso Carbon [Men]', 'Komperdell', 'Advanced men''s', 'c-pole', 'POLE-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL014', 'Leki Detect S [Men]', 'Leki', 'Intermediate men''s', 'c-pole', 'POLE-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL015', 'Salomon Arctic Lady [Women]', 'Salomon', 'Intermediate women''s', 'c-pole', 'POLE-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL016', 'Black Diamond Expedition 2 [Unisex]', 'Black Diamond', 'Unisex backcountry', 'c-pole', 'POLE-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL017', 'Scott Cascade C [Unisex]', 'Scott', 'Unisex carbon', 'c-pole', 'POLE-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL018', 'Atomic Cloud W [Women]', 'Atomic', 'Women''s on-piste', 'c-pole', 'POLE-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL019', 'Rossignol Electra [Women]', 'Rossignol', 'Women''s on-piste', 'c-pole', 'POLE-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PPL020', 'Leki Rider Jr [Junior]', 'Leki', 'Junior', 'c-pole', 'POLE-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

  -- Wax (c-wax)
  ('PWX001', 'Swix HS6 Blue -6/-12℃', 'Swix', 'Advanced racing cold', 'c-wax', 'WAX-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX002', 'Swix HS7 Violet -2/-8℃', 'Swix', 'Advanced racing medium-cold', 'c-wax', 'WAX-002', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX003', 'Swix HS8 Red -4/+4℃', 'Swix', 'Advanced racing medium', 'c-wax', 'WAX-003', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX004', 'Swix HS10 Yellow 0/+10℃', 'Swix', 'Advanced racing warm', 'c-wax', 'WAX-004', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX005', 'Swix PS6 Blue -6/-12℃', 'Swix', 'Intermediate maintenance cold', 'c-wax', 'WAX-005', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX006', 'Swix PS7 Violet -2/-8℃', 'Swix', 'Intermediate maintenance medium-cold', 'c-wax', 'WAX-006', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX007', 'Swix PS8 Red -4/+4℃', 'Swix', 'Intermediate maintenance medium', 'c-wax', 'WAX-007', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX008', 'Swix PS10 Yellow 0/+10℃', 'Swix', 'Intermediate maintenance warm', 'c-wax', 'WAX-008', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX009', 'Holmenkol Betamix Red -4/-14℃', 'Holmenkol', 'Advanced racing', 'c-wax', 'WAX-009', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX010', 'Holmenkol Ultramix Blue -8/-20℃', 'Holmenkol', 'Advanced racing cold', 'c-wax', 'WAX-010', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX011', 'TOKO NF Blue -10/-30℃', 'TOKO', 'Intermediate maintenance cold', 'c-wax', 'WAX-011', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX012', 'TOKO NF Red -4/-12℃', 'TOKO', 'Intermediate maintenance medium', 'c-wax', 'WAX-012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX013', 'Gallium Base Wax Pink -3/+5℃', 'Gallium', 'Beginner maintenance', 'c-wax', 'WAX-013', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX014', 'Hertel Hot Sauce All Temp', 'Hertel', 'Beginner all-around', 'c-wax', 'WAX-014', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX015', 'Purl Purple All Temp', 'Purl', 'Beginner all-around', 'c-wax', 'WAX-015', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX016', 'Dominator Zoom Lime 0/+10℃', 'Dominator', 'Advanced warm', 'c-wax', 'WAX-016', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX017', 'Briko-Maplus BP1 Med -5/+5℃', 'Briko-Maplus', 'Intermediate all-around', 'c-wax', 'WAX-017', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX018', 'OneBall Jay Fluoro-Free All Temp', 'OneBall', 'Beginner all-around', 'c-wax', 'WAX-018', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX019', 'Swix F4 Universal Glidewax', 'Swix', 'Beginner liquid', 'c-wax', 'WAX-019', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('PWX020', 'TOKO Express Pocket Warm', 'TOKO', 'Beginner liquid warm', 'c-wax', 'WAX-020', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO prices(id, product_id, regular_price, sale_price, currency_code, sale_start_date, sale_end_date) VALUES
  ('price-1', 'P001', 325.00, NULL, 'USD', NULL, NULL);

INSERT INTO prices(id, product_id, regular_price, sale_price, currency_code, sale_start_date, sale_end_date) VALUES
  -- Skis
  ('price-PSK001', 'PSK001', 832.00, NULL, 'USD', NULL, NULL),
  ('price-PSK002', 'PSK002', 812.50, NULL, 'USD', NULL, NULL),
  ('price-PSK003', 'PSK003', 780.00, NULL, 'USD', NULL, NULL),
  ('price-PSK004', 'PSK004', 637.00, NULL, 'USD', NULL, NULL),
  ('price-PSK005', 'PSK005', 598.00, NULL, 'USD', NULL, NULL),
  ('price-PSK006', 'PSK006', 877.50, NULL, 'USD', NULL, NULL),
  ('price-PSK007', 'PSK007', 767.00, NULL, 'USD', NULL, NULL),
  ('price-PSK008', 'PSK008', 624.00, NULL, 'USD', NULL, NULL),
  ('price-PSK009', 'PSK009', 442.00, NULL, 'USD', NULL, NULL),
  ('price-PSK010', 'PSK010', 747.50, NULL, 'USD', NULL, NULL),
  ('price-PSK011', 'PSK011', 728.00, NULL, 'USD', NULL, NULL),
  ('price-PSK012', 'PSK012', 767.00, NULL, 'USD', NULL, NULL),
  ('price-PSK013', 'PSK013', 637.00, NULL, 'USD', NULL, NULL),
  ('price-PSK014', 'PSK014', 598.00, NULL, 'USD', NULL, NULL),
  ('price-PSK015', 'PSK015', 507.00, NULL, 'USD', NULL, NULL),
  ('price-PSK016', 'PSK016', 617.50, NULL, 'USD', NULL, NULL),
  ('price-PSK017', 'PSK017', 643.50, NULL, 'USD', NULL, NULL),
  ('price-PSK018', 'PSK018', 468.00, NULL, 'USD', NULL, NULL),
  ('price-PSK019', 'PSK019', 455.00, NULL, 'USD', NULL, NULL),
  ('price-PSK020', 'PSK020', 292.50, NULL, 'USD', NULL, NULL),
  -- Boots
  ('price-PBT001', 'PBT001', 442.00, NULL, 'USD', NULL, NULL),
  ('price-PBT002', 'PBT002', 422.50, NULL, 'USD', NULL, NULL),
  ('price-PBT003', 'PBT003', 403.00, NULL, 'USD', NULL, NULL),
  ('price-PBT004', 'PBT004', 390.00, NULL, 'USD', NULL, NULL),
  ('price-PBT005', 'PBT005', 468.00, NULL, 'USD', NULL, NULL),
  ('price-PBT006', 'PBT006', 442.00, NULL, 'USD', NULL, NULL),
  ('price-PBT007', 'PBT007', 481.00, NULL, 'USD', NULL, NULL),
  ('price-PBT008', 'PBT008', 455.00, NULL, 'USD', NULL, NULL),
  ('price-PBT009', 'PBT009', 422.50, NULL, 'USD', NULL, NULL),
  ('price-PBT010', 'PBT010', 409.50, NULL, 'USD', NULL, NULL),
  ('price-PBT011', 'PBT011', 416.00, NULL, 'USD', NULL, NULL),
  ('price-PBT012', 'PBT012', 403.00, NULL, 'USD', NULL, NULL),
  ('price-PBT013', 'PBT013', 468.00, NULL, 'USD', NULL, NULL),
  ('price-PBT014', 'PBT014', 455.00, NULL, 'USD', NULL, NULL),
  ('price-PBT015', 'PBT015', 429.00, NULL, 'USD', NULL, NULL),
  ('price-PBT016', 'PBT016', 416.00, NULL, 'USD', NULL, NULL),
  ('price-PBT017', 'PBT017', 338.00, NULL, 'USD', NULL, NULL),
  ('price-PBT018', 'PBT018', 325.00, NULL, 'USD', NULL, NULL),
  ('price-PBT019', 'PBT019', 312.00, NULL, 'USD', NULL, NULL),
  ('price-PBT020', 'PBT020', 299.00, NULL, 'USD', NULL, NULL),
  -- Wear
  ('price-PWR001', 'PWR001', 637.00, NULL, 'USD', NULL, NULL),
  ('price-PWR002', 'PWR002', 617.50, NULL, 'USD', NULL, NULL),
  ('price-PWR003', 'PWR003', 442.00, NULL, 'USD', NULL, NULL),
  ('price-PWR004', 'PWR004', 422.50, NULL, 'USD', NULL, NULL),
  ('price-PWR005', 'PWR005', 585.00, NULL, 'USD', NULL, NULL),
  ('price-PWR006', 'PWR006', 572.00, NULL, 'USD', NULL, NULL),
  ('price-PWR007', 'PWR007', 539.50, NULL, 'USD', NULL, NULL),
  ('price-PWR008', 'PWR008', 468.00, NULL, 'USD', NULL, NULL),
  ('price-PWR009', 'PWR009', 507.00, NULL, 'USD', NULL, NULL),
  ('price-PWR010', 'PWR010', 468.00, NULL, 'USD', NULL, NULL),
  ('price-PWR011', 'PWR011', 552.50, NULL, 'USD', NULL, NULL),
  ('price-PWR012', 'PWR012', 539.50, NULL, 'USD', NULL, NULL),
  ('price-PWR013', 'PWR013', 338.00, NULL, 'USD', NULL, NULL),
  ('price-PWR014', 'PWR014', 325.00, NULL, 'USD', NULL, NULL),
  ('price-PWR015', 'PWR015', 325.00, NULL, 'USD', NULL, NULL),
  ('price-PWR016', 'PWR016', 312.00, NULL, 'USD', NULL, NULL),
  ('price-PWR017', 'PWR017', 338.00, NULL, 'USD', NULL, NULL),
  ('price-PWR018', 'PWR018', 325.00, NULL, 'USD', NULL, NULL),
  ('price-PWR019', 'PWR019', 247.00, NULL, 'USD', NULL, NULL),
  ('price-PWR020', 'PWR020', 234.00, NULL, 'USD', NULL, NULL),
  -- Helmet
  ('price-PHL001', 'PHL001', 208.00, NULL, 'USD', NULL, NULL),
  ('price-PHL002', 'PHL002', 201.50, NULL, 'USD', NULL, NULL),
  ('price-PHL003', 'PHL003', 195.00, NULL, 'USD', NULL, NULL),
  ('price-PHL004', 'PHL004', 188.50, NULL, 'USD', NULL, NULL),
  ('price-PHL005', 'PHL005', 182.00, NULL, 'USD', NULL, NULL),
  ('price-PHL006', 'PHL006', 175.50, NULL, 'USD', NULL, NULL),
  ('price-PHL007', 'PHL007', 195.00, NULL, 'USD', NULL, NULL),
  ('price-PHL008', 'PHL008', 191.75, NULL, 'USD', NULL, NULL),
  ('price-PHL009', 'PHL009', 169.00, NULL, 'USD', NULL, NULL),
  ('price-PHL010', 'PHL010', 162.50, NULL, 'USD', NULL, NULL),
  ('price-PHL011', 'PHL011', 182.00, NULL, 'USD', NULL, NULL),
  ('price-PHL012', 'PHL012', 117.00, NULL, 'USD', NULL, NULL),
  ('price-PHL013', 'PHL013', 162.50, NULL, 'USD', NULL, NULL),
  ('price-PHL014', 'PHL014', 156.00, NULL, 'USD', NULL, NULL),
  ('price-PHL015', 'PHL015', 214.50, NULL, 'USD', NULL, NULL),
  ('price-PHL016', 'PHL016', 208.00, NULL, 'USD', NULL, NULL),
  ('price-PHL017', 'PHL017', 104.00, NULL, 'USD', NULL, NULL),
  ('price-PHL018', 'PHL018', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PHL019', 'PHL019', 91.00, NULL, 'USD', NULL, NULL),
  ('price-PHL020', 'PHL020', 130.00, NULL, 'USD', NULL, NULL),
  -- Gloves
  ('price-PGL001', 'PGL001', 143.00, NULL, 'USD', NULL, NULL),
  ('price-PGL002', 'PGL002', 136.50, NULL, 'USD', NULL, NULL),
  ('price-PGL003', 'PGL003', 130.00, NULL, 'USD', NULL, NULL),
  ('price-PGL004', 'PGL004', 130.00, NULL, 'USD', NULL, NULL),
  ('price-PGL005', 'PGL005', 123.50, NULL, 'USD', NULL, NULL),
  ('price-PGL006', 'PGL006', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PGL007', 'PGL007', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PGL008', 'PGL008', 117.00, NULL, 'USD', NULL, NULL),
  ('price-PGL009', 'PGL009', 91.00, NULL, 'USD', NULL, NULL),
  ('price-PGL010', 'PGL010', 91.00, NULL, 'USD', NULL, NULL),
  ('price-PGL011', 'PGL011', 130.00, NULL, 'USD', NULL, NULL),
  ('price-PGL012', 'PGL012', 104.00, NULL, 'USD', NULL, NULL),
  ('price-PGL013', 'PGL013', 91.00, NULL, 'USD', NULL, NULL),
  ('price-PGL014', 'PGL014', 84.50, NULL, 'USD', NULL, NULL),
  ('price-PGL015', 'PGL015', 58.50, NULL, 'USD', NULL, NULL),
  ('price-PGL016', 'PGL016', 71.50, NULL, 'USD', NULL, NULL),
  ('price-PGL017', 'PGL017', 58.50, NULL, 'USD', NULL, NULL),
  ('price-PGL018', 'PGL018', 84.50, NULL, 'USD', NULL, NULL),
  ('price-PGL019', 'PGL019', 84.50, NULL, 'USD', NULL, NULL),
  ('price-PGL020', 'PGL020', 110.50, NULL, 'USD', NULL, NULL),
  -- Poles
  ('price-PPL001', 'PPL001', 143.00, NULL, 'USD', NULL, NULL),
  ('price-PPL002', 'PPL002', 104.00, NULL, 'USD', NULL, NULL),
  ('price-PPL003', 'PPL003', 156.00, NULL, 'USD', NULL, NULL),
  ('price-PPL004', 'PPL004', 162.50, NULL, 'USD', NULL, NULL),
  ('price-PPL005', 'PPL005', 162.50, NULL, 'USD', NULL, NULL),
  ('price-PPL006', 'PPL006', 130.00, NULL, 'USD', NULL, NULL),
  ('price-PPL007', 'PPL007', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PPL008', 'PPL008', 117.00, NULL, 'USD', NULL, NULL),
  ('price-PPL009', 'PPL009', 91.00, NULL, 'USD', NULL, NULL),
  ('price-PPL010', 'PPL010', 104.00, NULL, 'USD', NULL, NULL),
  ('price-PPL011', 'PPL011', 117.00, NULL, 'USD', NULL, NULL),
  ('price-PPL012', 'PPL012', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PPL013', 'PPL013', 130.00, NULL, 'USD', NULL, NULL),
  ('price-PPL014', 'PPL014', 110.50, NULL, 'USD', NULL, NULL),
  ('price-PPL015', 'PPL015', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PPL016', 'PPL016', 117.00, NULL, 'USD', NULL, NULL),
  ('price-PPL017', 'PPL017', 113.75, NULL, 'USD', NULL, NULL),
  ('price-PPL018', 'PPL018', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PPL019', 'PPL019', 97.50, NULL, 'USD', NULL, NULL),
  ('price-PPL020', 'PPL020', 52.00, NULL, 'USD', NULL, NULL),
  -- Wax
  ('price-PWX001', 'PWX001', 19.50, NULL, 'USD', NULL, NULL),
  ('price-PWX002', 'PWX002', 19.50, NULL, 'USD', NULL, NULL),
  ('price-PWX003', 'PWX003', 19.50, NULL, 'USD', NULL, NULL),
  ('price-PWX004', 'PWX004', 19.50, NULL, 'USD', NULL, NULL),
  ('price-PWX005', 'PWX005', 13.00, NULL, 'USD', NULL, NULL),
  ('price-PWX006', 'PWX006', 13.00, NULL, 'USD', NULL, NULL),
  ('price-PWX007', 'PWX007', 13.00, NULL, 'USD', NULL, NULL),
  ('price-PWX008', 'PWX008', 13.00, NULL, 'USD', NULL, NULL),
  ('price-PWX009', 'PWX009', 16.25, NULL, 'USD', NULL, NULL),
  ('price-PWX010', 'PWX010', 16.25, NULL, 'USD', NULL, NULL),
  ('price-PWX011', 'PWX011', 11.70, NULL, 'USD', NULL, NULL),
  ('price-PWX012', 'PWX012', 11.70, NULL, 'USD', NULL, NULL),
  ('price-PWX013', 'PWX013', 9.75, NULL, 'USD', NULL, NULL),
  ('price-PWX014', 'PWX014', 9.75, NULL, 'USD', NULL, NULL),
  ('price-PWX015', 'PWX015', 9.75, NULL, 'USD', NULL, NULL),
  ('price-PWX016', 'PWX016', 14.30, NULL, 'USD', NULL, NULL),
  ('price-PWX017', 'PWX017', 13.00, NULL, 'USD', NULL, NULL),
  ('price-PWX018', 'PWX018', 10.40, NULL, 'USD', NULL, NULL),
  ('price-PWX019', 'PWX019', 10.40, NULL, 'USD', NULL, NULL),
  ('price-PWX020', 'PWX020', 10.40, NULL, 'USD', NULL, NULL);

INSERT INTO inventory(id, product_id, quantity, reserved_quantity, status) VALUES
  ('inv-1', 'P001', 10, 0, 'AVAILABLE');

INSERT INTO inventory(id, product_id, quantity, reserved_quantity, status) VALUES
  -- Skis
  ('inv-PSK001', 'PSK001', 20, 0, 'AVAILABLE'),
  ('inv-PSK002', 'PSK002', 20, 0, 'AVAILABLE'),
  ('inv-PSK003', 'PSK003', 20, 0, 'AVAILABLE'),
  ('inv-PSK004', 'PSK004', 20, 0, 'AVAILABLE'),
  ('inv-PSK005', 'PSK005', 20, 0, 'AVAILABLE'),
  ('inv-PSK006', 'PSK006', 20, 0, 'AVAILABLE'),
  ('inv-PSK007', 'PSK007', 20, 0, 'AVAILABLE'),
  ('inv-PSK008', 'PSK008', 20, 0, 'AVAILABLE'),
  ('inv-PSK009', 'PSK009', 20, 0, 'AVAILABLE'),
  ('inv-PSK010', 'PSK010', 20, 0, 'AVAILABLE'),
  ('inv-PSK011', 'PSK011', 20, 0, 'AVAILABLE'),
  ('inv-PSK012', 'PSK012', 20, 0, 'AVAILABLE'),
  ('inv-PSK013', 'PSK013', 20, 0, 'AVAILABLE'),
  ('inv-PSK014', 'PSK014', 20, 0, 'AVAILABLE'),
  ('inv-PSK015', 'PSK015', 20, 0, 'AVAILABLE'),
  ('inv-PSK016', 'PSK016', 20, 0, 'AVAILABLE'),
  ('inv-PSK017', 'PSK017', 20, 0, 'AVAILABLE'),
  ('inv-PSK018', 'PSK018', 20, 0, 'AVAILABLE'),
  ('inv-PSK019', 'PSK019', 20, 0, 'AVAILABLE'),
  ('inv-PSK020', 'PSK020', 20, 0, 'AVAILABLE'),
  -- Boots
  ('inv-PBT001', 'PBT001', 20, 0, 'AVAILABLE'),
  ('inv-PBT002', 'PBT002', 20, 0, 'AVAILABLE'),
  ('inv-PBT003', 'PBT003', 20, 0, 'AVAILABLE'),
  ('inv-PBT004', 'PBT004', 20, 0, 'AVAILABLE'),
  ('inv-PBT005', 'PBT005', 20, 0, 'AVAILABLE'),
  ('inv-PBT006', 'PBT006', 20, 0, 'AVAILABLE'),
  ('inv-PBT007', 'PBT007', 20, 0, 'AVAILABLE'),
  ('inv-PBT008', 'PBT008', 20, 0, 'AVAILABLE'),
  ('inv-PBT009', 'PBT009', 20, 0, 'AVAILABLE'),
  ('inv-PBT010', 'PBT010', 20, 0, 'AVAILABLE'),
  ('inv-PBT011', 'PBT011', 20, 0, 'AVAILABLE'),
  ('inv-PBT012', 'PBT012', 20, 0, 'AVAILABLE'),
  ('inv-PBT013', 'PBT013', 20, 0, 'AVAILABLE'),
  ('inv-PBT014', 'PBT014', 20, 0, 'AVAILABLE'),
  ('inv-PBT015', 'PBT015', 20, 0, 'AVAILABLE'),
  ('inv-PBT016', 'PBT016', 20, 0, 'AVAILABLE'),
  ('inv-PBT017', 'PBT017', 20, 0, 'AVAILABLE'),
  ('inv-PBT018', 'PBT018', 20, 0, 'AVAILABLE'),
  ('inv-PBT019', 'PBT019', 20, 0, 'AVAILABLE'),
  ('inv-PBT020', 'PBT020', 20, 0, 'AVAILABLE'),
  -- Wear
  ('inv-PWR001', 'PWR001', 20, 0, 'AVAILABLE'),
  ('inv-PWR002', 'PWR002', 20, 0, 'AVAILABLE'),
  ('inv-PWR003', 'PWR003', 20, 0, 'AVAILABLE'),
  ('inv-PWR004', 'PWR004', 20, 0, 'AVAILABLE'),
  ('inv-PWR005', 'PWR005', 20, 0, 'AVAILABLE'),
  ('inv-PWR006', 'PWR006', 20, 0, 'AVAILABLE'),
  ('inv-PWR007', 'PWR007', 20, 0, 'AVAILABLE'),
  ('inv-PWR008', 'PWR008', 20, 0, 'AVAILABLE'),
  ('inv-PWR009', 'PWR009', 20, 0, 'AVAILABLE'),
  ('inv-PWR010', 'PWR010', 20, 0, 'AVAILABLE'),
  ('inv-PWR011', 'PWR011', 20, 0, 'AVAILABLE'),
  ('inv-PWR012', 'PWR012', 20, 0, 'AVAILABLE'),
  ('inv-PWR013', 'PWR013', 20, 0, 'AVAILABLE'),
  ('inv-PWR014', 'PWR014', 20, 0, 'AVAILABLE'),
  ('inv-PWR015', 'PWR015', 20, 0, 'AVAILABLE'),
  ('inv-PWR016', 'PWR016', 20, 0, 'AVAILABLE'),
  ('inv-PWR017', 'PWR017', 20, 0, 'AVAILABLE'),
  ('inv-PWR018', 'PWR018', 20, 0, 'AVAILABLE'),
  ('inv-PWR019', 'PWR019', 20, 0, 'AVAILABLE'),
  ('inv-PWR020', 'PWR020', 20, 0, 'AVAILABLE'),
  -- Helmet
  ('inv-PHL001', 'PHL001', 20, 0, 'AVAILABLE'),
  ('inv-PHL002', 'PHL002', 20, 0, 'AVAILABLE'),
  ('inv-PHL003', 'PHL003', 20, 0, 'AVAILABLE'),
  ('inv-PHL004', 'PHL004', 20, 0, 'AVAILABLE'),
  ('inv-PHL005', 'PHL005', 20, 0, 'AVAILABLE'),
  ('inv-PHL006', 'PHL006', 20, 0, 'AVAILABLE'),
  ('inv-PHL007', 'PHL007', 20, 0, 'AVAILABLE'),
  ('inv-PHL008', 'PHL008', 20, 0, 'AVAILABLE'),
  ('inv-PHL009', 'PHL009', 20, 0, 'AVAILABLE'),
  ('inv-PHL010', 'PHL010', 20, 0, 'AVAILABLE'),
  ('inv-PHL011', 'PHL011', 20, 0, 'AVAILABLE'),
  ('inv-PHL012', 'PHL012', 20, 0, 'AVAILABLE'),
  ('inv-PHL013', 'PHL013', 20, 0, 'AVAILABLE'),
  ('inv-PHL014', 'PHL014', 20, 0, 'AVAILABLE'),
  ('inv-PHL015', 'PHL015', 20, 0, 'AVAILABLE'),
  ('inv-PHL016', 'PHL016', 20, 0, 'AVAILABLE'),
  ('inv-PHL017', 'PHL017', 20, 0, 'AVAILABLE'),
  ('inv-PHL018', 'PHL018', 20, 0, 'AVAILABLE'),
  ('inv-PHL019', 'PHL019', 20, 0, 'AVAILABLE'),
  ('inv-PHL020', 'PHL020', 20, 0, 'AVAILABLE'),
  -- Gloves
  ('inv-PGL001', 'PGL001', 20, 0, 'AVAILABLE'),
  ('inv-PGL002', 'PGL002', 20, 0, 'AVAILABLE'),
  ('inv-PGL003', 'PGL003', 20, 0, 'AVAILABLE'),
  ('inv-PGL004', 'PGL004', 20, 0, 'AVAILABLE'),
  ('inv-PGL005', 'PGL005', 20, 0, 'AVAILABLE'),
  ('inv-PGL006', 'PGL006', 20, 0, 'AVAILABLE'),
  ('inv-PGL007', 'PGL007', 20, 0, 'AVAILABLE'),
  ('inv-PGL008', 'PGL008', 20, 0, 'AVAILABLE'),
  ('inv-PGL009', 'PGL009', 20, 0, 'AVAILABLE'),
  ('inv-PGL010', 'PGL010', 20, 0, 'AVAILABLE'),
  ('inv-PGL011', 'PGL011', 20, 0, 'AVAILABLE'),
  ('inv-PGL012', 'PGL012', 20, 0, 'AVAILABLE'),
  ('inv-PGL013', 'PGL013', 20, 0, 'AVAILABLE'),
  ('inv-PGL014', 'PGL014', 20, 0, 'AVAILABLE'),
  ('inv-PGL015', 'PGL015', 20, 0, 'AVAILABLE'),
  ('inv-PGL016', 'PGL016', 20, 0, 'AVAILABLE'),
  ('inv-PGL017', 'PGL017', 20, 0, 'AVAILABLE'),
  ('inv-PGL018', 'PGL018', 20, 0, 'AVAILABLE'),
  ('inv-PGL019', 'PGL019', 20, 0, 'AVAILABLE'),
  ('inv-PGL020', 'PGL020', 20, 0, 'AVAILABLE'),
  -- Poles
  ('inv-PPL001', 'PPL001', 20, 0, 'AVAILABLE'),
  ('inv-PPL002', 'PPL002', 20, 0, 'AVAILABLE'),
  ('inv-PPL003', 'PPL003', 20, 0, 'AVAILABLE'),
  ('inv-PPL004', 'PPL004', 20, 0, 'AVAILABLE'),
  ('inv-PPL005', 'PPL005', 20, 0, 'AVAILABLE'),
  ('inv-PPL006', 'PPL006', 20, 0, 'AVAILABLE'),
  ('inv-PPL007', 'PPL007', 20, 0, 'AVAILABLE'),
  ('inv-PPL008', 'PPL008', 20, 0, 'AVAILABLE'),
  ('inv-PPL009', 'PPL009', 20, 0, 'AVAILABLE'),
  ('inv-PPL010', 'PPL010', 20, 0, 'AVAILABLE'),
  ('inv-PPL011', 'PPL011', 20, 0, 'AVAILABLE'),
  ('inv-PPL012', 'PPL012', 20, 0, 'AVAILABLE'),
  ('inv-PPL013', 'PPL013', 20, 0, 'AVAILABLE'),
  ('inv-PPL014', 'PPL014', 20, 0, 'AVAILABLE'),
  ('inv-PPL015', 'PPL015', 20, 0, 'AVAILABLE'),
  ('inv-PPL016', 'PPL016', 20, 0, 'AVAILABLE'),
  ('inv-PPL017', 'PPL017', 20, 0, 'AVAILABLE'),
  ('inv-PPL018', 'PPL018', 20, 0, 'AVAILABLE'),
  ('inv-PPL019', 'PPL019', 20, 0, 'AVAILABLE'),
  ('inv-PPL020', 'PPL020', 20, 0, 'AVAILABLE'),
  -- Wax
  ('inv-PWX001', 'PWX001', 50, 0, 'AVAILABLE'),
  ('inv-PWX002', 'PWX002', 50, 0, 'AVAILABLE'),
  ('inv-PWX003', 'PWX003', 50, 0, 'AVAILABLE'),
  ('inv-PWX004', 'PWX004', 50, 0, 'AVAILABLE'),
  ('inv-PWX005', 'PWX005', 50, 0, 'AVAILABLE'),
  ('inv-PWX006', 'PWX006', 50, 0, 'AVAILABLE'),
  ('inv-PWX007', 'PWX007', 50, 0, 'AVAILABLE'),
  ('inv-PWX008', 'PWX008', 50, 0, 'AVAILABLE'),
  ('inv-PWX009', 'PWX009', 50, 0, 'AVAILABLE'),
  ('inv-PWX010', 'PWX010', 50, 0, 'AVAILABLE'),
  ('inv-PWX011', 'PWX011', 50, 0, 'AVAILABLE'),
  ('inv-PWX012', 'PWX012', 50, 0, 'AVAILABLE'),
  ('inv-PWX013', 'PWX013', 50, 0, 'AVAILABLE'),
  ('inv-PWX014', 'PWX014', 50, 0, 'AVAILABLE'),
  ('inv-PWX015', 'PWX015', 50, 0, 'AVAILABLE'),
  ('inv-PWX016', 'PWX016', 50, 0, 'AVAILABLE'),
  ('inv-PWX017', 'PWX017', 50, 0, 'AVAILABLE'),
  ('inv-PWX018', 'PWX018', 50, 0, 'AVAILABLE'),
  ('inv-PWX019', 'PWX019', 50, 0, 'AVAILABLE'),
  ('inv-PWX020', 'PWX020', 50, 0, 'AVAILABLE');

INSERT INTO carts(id, user_id, session_id, status, expires_at) VALUES
  ('cart-1', 'u-1', 'session-1', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO cart_items(id, cart_id, product_id, quantity, unit_price) VALUES
  ('cart-item-1', 'cart-1', 'P001', 1, 325.00);

INSERT INTO payments(id, order_id, cart_id, amount, currency, status, payment_intent_id, created_at) VALUES
  ('pay-1', 'order-1', 'cart-1', 362.70, 'USD', 'AUTHORIZED', 'PAY-0001', CURRENT_TIMESTAMP);

INSERT INTO orders(id, order_number, user_id, status, payment_status, subtotal, tax, shipping_fee, discount_amount, total_amount, coupon_code, used_points, created_at, updated_at) VALUES
  ('order-1', 'ORD-0001', 'u-1', 'CREATED', 'AUTHORIZED', 325.00, 32.50, 5.20, 0, 362.70, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items(id, order_id, product_id, product_name, sku, unit_price, quantity, subtotal) VALUES
  ('order-item-1', 'order-1', 'P001', 'Ski A', 'SKU-001', 325.00, 1, 325.00);

INSERT INTO shipments(id, order_id, carrier, tracking_number, status, shipped_at, delivered_at) VALUES
  ('ship-1', 'order-1', 'Yamato', 'TRK-001', 'PENDING', NULL, NULL);

INSERT INTO returns(id, order_id, order_item_id, reason, quantity, refund_amount, status) VALUES
  ('return-1', 'order-1', 'order-item-1', 'size', 1, 325.00, 'REQUESTED');

INSERT INTO order_shipping(id, order_id, recipient_name, postal_code, prefecture, address1, address2, phone, shipping_method_code, shipping_fee, requested_delivery_date) VALUES
  ('os-1', 'order-1', 'Taro Yamada', '160-0022', 'Tokyo', '1-1-1 Shinjuku, Shinjuku-ku', 'Apartment 101', '0312345678', 'STANDARD', 5.20, NULL);

INSERT INTO point_accounts(id, user_id, balance, lifetime_earned, lifetime_redeemed) VALUES
  ('pa-1', 'u-1', 100, 100, 0);

INSERT INTO point_transactions(id, user_id, type, amount, reference_id, description, expires_at, is_expired, created_at) VALUES
  ('pt-1', 'u-1', 'EARN', 100, 'order-1', 'Initial points', CURRENT_TIMESTAMP, FALSE, CURRENT_TIMESTAMP);

INSERT INTO campaigns(id, name, description, type, start_date, end_date, is_active, rules_json) VALUES
  ('camp-1', 'Winter', 'Winter campaign', 'SEASONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, '{}');

INSERT INTO coupons(id, campaign_id, code, coupon_type, discount_value, discount_type, minimum_amount, maximum_discount, usage_limit, used_count, is_active, expires_at) VALUES
  ('coupon-1', 'camp-1', 'SAVE10', 'PERCENT', 10, 'ORDER', 6.50, 32.50, 100, 0, TRUE, CURRENT_TIMESTAMP);

INSERT INTO user_addresses(id, user_id, label, recipient_name, postal_code, prefecture, address1, address2, phone, is_default, created_at, updated_at) VALUES
  ('addr-1', 'u-1', 'Home', 'Taro Yamada', '160-0022', 'Tokyo', '1-1-1 Shinjuku, Shinjuku-ku', 'Apartment 101', '0312345678', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO password_reset_tokens(id, user_id, token, expires_at, used_at) VALUES
  ('prt-1', 'u-1', 'token-1', CURRENT_TIMESTAMP, NULL);

INSERT INTO shipping_methods(id, code, name, fee, is_active, sort_order) VALUES
  ('ship-std', 'STANDARD', 'Standard', 5.20, TRUE, 1);

INSERT INTO email_queue(id, to_addr, subject, body, status, retry_count, last_error, scheduled_at, sent_at) VALUES
  ('mail-1', 'user@example.com', 'Welcome', 'Body', 'PENDING', 0, NULL, CURRENT_TIMESTAMP, NULL);
