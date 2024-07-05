--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: check; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "check" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE "check" OWNER TO postgres;

\connect "check"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: discount_card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount_card (
    id bigint NOT NULL,
    number integer NOT NULL,
    amount smallint NOT NULL,
    CONSTRAINT chk_discount_amount CHECK (((amount >= 0) AND (amount <= 100)))
);


ALTER TABLE public.discount_card OWNER TO postgres;

--
-- Name: discount_card_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_card_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.discount_card_id_seq OWNER TO postgres;

--
-- Name: discount_card_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discount_card_id_seq OWNED BY public.discount_card.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id bigint NOT NULL,
    description character varying(50) NOT NULL,
    price numeric(4,2) NOT NULL,
    quantity_in_stock integer NOT NULL,
    wholesale_product boolean NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: discount_card id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_card ALTER COLUMN id SET DEFAULT nextval('public.discount_card_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Data for Name: discount_card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount_card (id, number, amount) FROM stdin;
1	1111	3
2	2222	3
3	3333	4
4	4444	5
5	9999	2
6	1001	2
7	1002	2
8	1003	2
9	1004	2
10	1005	2
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, description, price, quantity_in_stock, wholesale_product) FROM stdin;
1	Milk	1.07	10	t
3	Yogurt 400g	2.10	7	t
4	Packed potatoes 1kg	1.47	30	f
5	Packed cabbage 1kg	1.19	15	f
6	Packed tomatoes 350g	1.60	50	f
7	Packed apples 1kg	2.78	18	f
8	Packed oranges 1kg	3.20	12	f
9	Packed bananas 1kg	1.10	25	t
10	Packed beef fillet 1kg	12.80	7	f
11	Packed pork fillet 1kg	8.52	14	f
12	Packed chicken breasts 1kgSour	10.75	18	f
13	Baguette 360g	1.30	10	t
14	Drinking water 1,5l	0.80	100	f
15	Olive oil 500ml	5.30	16	f
16	Sunflower oil 1l	1.20	12	f
17	Chocolate Ritter sport 100g	1.10	50	t
18	Paulaner 0,5l	1.10	100	f
19	Whiskey Jim Beam 1l	13.99	30	f
20	Whiskey Jack Daniels 1l	17.19	20	f
2	Cream 400g	2.71	20	t
\.


--
-- Name: discount_card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_card_id_seq', 1, false);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 1, false);


--
-- Name: discount_card discount_card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_card
    ADD CONSTRAINT discount_card_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

