insert INTO users(name, username, password, email, role, blocked) VALUES 
('ysz', 'ysz', '$2a$10$ZUPqPHo8/Pvu69avdsp.gOZ13wkb2NWb9g/7Nb.LO7Gm7.PcnPqSu', 'shengyang_zhou@hotmail.com', 'ROLE_SUPERUSER', 'false'),
('zsy', 'zsy', '$2a$10$HA86qJX24N2gUJjsD3kc/epYuHUq/ltt/iLWVQtYsJi4v4SLlyowy', 'shengyang_zhou@baylor.edu', 'ROLE_ADMIN', 'false')
on conflict (username) do nothing;