CREATE TABLE IF NOT EXISTS public.goods
(
    vendor_code integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 100000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(250) COLLATE pg_catalog."default" NOT NULL,
    price integer NOT NULL,
    images character varying(1024) COLLATE pg_catalog."default",
    CONSTRAINT goods_pkey PRIMARY KEY (vendor_code)
);

CREATE TABLE IF NOT EXISTS public.reviews
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    good_id integer NOT NULL,
    rating double precision NOT NULL,
    added_ago character varying(50) COLLATE pg_catalog."default",
    body character varying(300) COLLATE pg_catalog."default",
    CONSTRAINT reviews_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.tokens
   (
       id character varying(50) COLLATE pg_catalog."default" NOT NULL,
       token character varying(75) COLLATE pg_catalog."default" NOT NULL,
       user_id integer NOT NULL,
       CONSTRAINT tokens_pkey PRIMARY KEY (id)
   );

CREATE TABLE IF NOT EXISTS public.users
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    password character varying(25) COLLATE pg_catalog."default" NOT NULL,
    username character varying(25) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    avatar character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id, email)
);

CREATE TABLE IF NOT EXISTS public.verification_tokens
(
    token character varying(75) COLLATE pg_catalog."default" NOT NULL,
    code integer NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT verification_tokens_pkey PRIMARY KEY (token)
);