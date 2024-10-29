ALTER TABLE api_role ADD `inactive` bit(1) NULL;
ALTER TABLE api_role ADD `created_at` datetime NULL;
ALTER TABLE api_role ADD `created_user` bigint(11) NULL;
ALTER TABLE api_role ADD `updated_at` datetime NULL;
ALTER TABLE api_role ADD `updated_user` bigint(11) NULL;