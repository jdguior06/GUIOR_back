PGDMP  $    ,            	    |            Sistema_POS    16.4    16.4 r    c           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            d           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            e           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            f           1262    27797    Sistema_POS    DATABASE     �   CREATE DATABASE "Sistema_POS" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Bolivia.1252';
    DROP DATABASE "Sistema_POS";
                pos    false            �            1259    31812    almacen    TABLE     �   CREATE TABLE public.almacen (
    activo boolean NOT NULL,
    numero integer NOT NULL,
    id bigint NOT NULL,
    id_sucursal bigint,
    descripcion character varying(255)
);
    DROP TABLE public.almacen;
       public         heap    pos    false            �            1259    31797    almacen_seq    SEQUENCE     u   CREATE SEQUENCE public.almacen_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.almacen_seq;
       public          pos    false            �            1259    31817    cajas    TABLE     �   CREATE TABLE public.cajas (
    activo boolean NOT NULL,
    id bigint NOT NULL,
    id_sucursal bigint,
    nombre character varying(255)
);
    DROP TABLE public.cajas;
       public         heap    pos    false            �            1259    31798 	   cajas_seq    SEQUENCE     s   CREATE SEQUENCE public.cajas_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.cajas_seq;
       public          pos    false            �            1259    31822 	   categoria    TABLE     �   CREATE TABLE public.categoria (
    activo boolean NOT NULL,
    id bigint NOT NULL,
    nombre character varying(45) NOT NULL,
    descripcion character varying(1000)
);
    DROP TABLE public.categoria;
       public         heap    pos    false            �            1259    31799    categoria_seq    SEQUENCE     w   CREATE SEQUENCE public.categoria_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.categoria_seq;
       public          pos    false            �            1259    31831    cliente    TABLE     �   CREATE TABLE public.cliente (
    activo boolean NOT NULL,
    id bigint NOT NULL,
    apellido character varying(255),
    email character varying(255),
    nit character varying(255),
    nombre character varying(255)
);
    DROP TABLE public.cliente;
       public         heap    pos    false            �            1259    31800    cliente_seq    SEQUENCE     u   CREATE SEQUENCE public.cliente_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.cliente_seq;
       public          pos    false            �            1259    31840    detalle_nota_entrada    TABLE     �   CREATE TABLE public.detalle_nota_entrada (
    cantidad integer NOT NULL,
    costo_unitario real NOT NULL,
    sub_total real NOT NULL,
    id bigint NOT NULL,
    id_nota_entrada bigint,
    producto_id bigint
);
 (   DROP TABLE public.detalle_nota_entrada;
       public         heap    pos    false            �            1259    31801    detalle_nota_entrada_seq    SEQUENCE     �   CREATE SEQUENCE public.detalle_nota_entrada_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.detalle_nota_entrada_seq;
       public          pos    false            �            1259    31845    nota_entrada    TABLE     �   CREATE TABLE public.nota_entrada (
    descuento real NOT NULL,
    total real NOT NULL,
    fecha timestamp(6) without time zone,
    id bigint NOT NULL,
    id_almacen bigint,
    id_proveedor bigint
);
     DROP TABLE public.nota_entrada;
       public         heap    pos    false            �            1259    31802    nota_entrada_seq    SEQUENCE     z   CREATE SEQUENCE public.nota_entrada_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.nota_entrada_seq;
       public          pos    false            �            1259    31850    permiso    TABLE     [   CREATE TABLE public.permiso (
    id bigint NOT NULL,
    nombre character varying(255)
);
    DROP TABLE public.permiso;
       public         heap    pos    false            �            1259    31803    permiso_seq    SEQUENCE     u   CREATE SEQUENCE public.permiso_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.permiso_seq;
       public          pos    false            �            1259    31855    plan    TABLE       CREATE TABLE public.plan (
    costo real NOT NULL,
    limite_sucursales integer NOT NULL,
    limite_usuarios integer NOT NULL,
    id bigint NOT NULL,
    tipo character varying(20) NOT NULL,
    descripcion character varying(255),
    nombre character varying(255) NOT NULL
);
    DROP TABLE public.plan;
       public         heap    pos    false            �            1259    31804    plan_seq    SEQUENCE     r   CREATE SEQUENCE public.plan_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    DROP SEQUENCE public.plan_seq;
       public          pos    false            �            1259    31862    producto    TABLE     V  CREATE TABLE public.producto (
    activo boolean NOT NULL,
    precio_compra double precision NOT NULL,
    precio_venta double precision NOT NULL,
    id bigint NOT NULL,
    id_categoria bigint,
    codigo character varying(20),
    nombre character varying(50),
    descripcion character varying(1000),
    foto character varying(255)
);
    DROP TABLE public.producto;
       public         heap    pos    false            �            1259    31869    producto_almacen    TABLE     �   CREATE TABLE public.producto_almacen (
    activo boolean NOT NULL,
    stock integer NOT NULL,
    almacen_id bigint,
    id bigint NOT NULL,
    producto_id bigint
);
 $   DROP TABLE public.producto_almacen;
       public         heap    pos    false            �            1259    31805    producto_almacen_seq    SEQUENCE     ~   CREATE SEQUENCE public.producto_almacen_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.producto_almacen_seq;
       public          pos    false            �            1259    31806    producto_seq    SEQUENCE     v   CREATE SEQUENCE public.producto_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.producto_seq;
       public          pos    false            �            1259    31874 	   proveedor    TABLE     �   CREATE TABLE public.proveedor (
    activo boolean NOT NULL,
    id bigint NOT NULL,
    id_almacen bigint,
    direccion character varying(255),
    email character varying(255),
    nombre character varying(255),
    telefono character varying(255)
);
    DROP TABLE public.proveedor;
       public         heap    pos    false            �            1259    31807    proveedor_seq    SEQUENCE     w   CREATE SEQUENCE public.proveedor_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.proveedor_seq;
       public          pos    false            �            1259    31883    rol    TABLE     W   CREATE TABLE public.rol (
    id bigint NOT NULL,
    nombre character varying(255)
);
    DROP TABLE public.rol;
       public         heap    pos    false            �            1259    31888    rol_permiso    TABLE     `   CREATE TABLE public.rol_permiso (
    permiso_id bigint NOT NULL,
    rol_id bigint NOT NULL
);
    DROP TABLE public.rol_permiso;
       public         heap    pos    false            �            1259    31808    rol_seq    SEQUENCE     q   CREATE SEQUENCE public.rol_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    DROP SEQUENCE public.rol_seq;
       public          pos    false            �            1259    31894    sucursal    TABLE     	  CREATE TABLE public.sucursal (
    activo boolean NOT NULL,
    id bigint NOT NULL,
    codigo character varying(20),
    nit character varying(20),
    direccion character varying(255),
    nombre character varying(255),
    razon_social character varying(255)
);
    DROP TABLE public.sucursal;
       public         heap    pos    false            �            1259    31893    sucursal_id_seq    SEQUENCE     �   ALTER TABLE public.sucursal ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.sucursal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          pos    false    244            �            1259    31901 
   suscriptor    TABLE       CREATE TABLE public.suscriptor (
    estado boolean NOT NULL,
    fecha_final date NOT NULL,
    fecha_inicio date NOT NULL,
    id integer NOT NULL,
    id_plan bigint NOT NULL,
    id_usuario bigint NOT NULL,
    nombre character varying(255) NOT NULL
);
    DROP TABLE public.suscriptor;
       public         heap    pos    false            �            1259    31809    suscriptor_seq    SEQUENCE     x   CREATE SEQUENCE public.suscriptor_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.suscriptor_seq;
       public          pos    false            �            1259    31908    tarjeta    TABLE     A  CREATE TABLE public.tarjeta (
    id_suscriptor integer NOT NULL,
    "mes_año" character varying(5) NOT NULL,
    cvc character varying(6) NOT NULL,
    id bigint NOT NULL,
    nro_tarjeta character varying(30) NOT NULL,
    correo character varying(255) NOT NULL,
    nombre_titular character varying(255) NOT NULL
);
    DROP TABLE public.tarjeta;
       public         heap    pos    false            �            1259    31810    tarjeta_seq    SEQUENCE     u   CREATE SEQUENCE public.tarjeta_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.tarjeta_seq;
       public          pos    false            �            1259    31919    user_rol    TABLE     Z   CREATE TABLE public.user_rol (
    rol_id bigint NOT NULL,
    user_id bigint NOT NULL
);
    DROP TABLE public.user_rol;
       public         heap    pos    false            �            1259    31924    usuario    TABLE     g  CREATE TABLE public.usuario (
    activo boolean NOT NULL,
    credenciales_no_expiradas boolean NOT NULL,
    cuenta_no_bloqueada boolean NOT NULL,
    cuenta_no_expirada boolean NOT NULL,
    id bigint NOT NULL,
    apellido character varying(255),
    email character varying(255),
    nombre character varying(255),
    password character varying(255)
);
    DROP TABLE public.usuario;
       public         heap    pos    false            �            1259    31811    usuario_seq    SEQUENCE     u   CREATE SEQUENCE public.usuario_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.usuario_seq;
       public          pos    false            N          0    31812    almacen 
   TABLE DATA           O   COPY public.almacen (activo, numero, id, id_sucursal, descripcion) FROM stdin;
    public          pos    false    230   ݄       O          0    31817    cajas 
   TABLE DATA           @   COPY public.cajas (activo, id, id_sucursal, nombre) FROM stdin;
    public          pos    false    231   (�       P          0    31822 	   categoria 
   TABLE DATA           D   COPY public.categoria (activo, id, nombre, descripcion) FROM stdin;
    public          pos    false    232   [�       Q          0    31831    cliente 
   TABLE DATA           K   COPY public.cliente (activo, id, apellido, email, nit, nombre) FROM stdin;
    public          pos    false    233   ��       R          0    31840    detalle_nota_entrada 
   TABLE DATA           u   COPY public.detalle_nota_entrada (cantidad, costo_unitario, sub_total, id, id_nota_entrada, producto_id) FROM stdin;
    public          pos    false    234   ��       S          0    31845    nota_entrada 
   TABLE DATA           ]   COPY public.nota_entrada (descuento, total, fecha, id, id_almacen, id_proveedor) FROM stdin;
    public          pos    false    235   ��       T          0    31850    permiso 
   TABLE DATA           -   COPY public.permiso (id, nombre) FROM stdin;
    public          pos    false    236   ݅       U          0    31855    plan 
   TABLE DATA           h   COPY public.plan (costo, limite_sucursales, limite_usuarios, id, tipo, descripcion, nombre) FROM stdin;
    public          pos    false    237   ��       V          0    31862    producto 
   TABLE DATA           |   COPY public.producto (activo, precio_compra, precio_venta, id, id_categoria, codigo, nombre, descripcion, foto) FROM stdin;
    public          pos    false    238   �       W          0    31869    producto_almacen 
   TABLE DATA           V   COPY public.producto_almacen (activo, stock, almacen_id, id, producto_id) FROM stdin;
    public          pos    false    239   /�       X          0    31874 	   proveedor 
   TABLE DATA           _   COPY public.proveedor (activo, id, id_almacen, direccion, email, nombre, telefono) FROM stdin;
    public          pos    false    240   L�       Y          0    31883    rol 
   TABLE DATA           )   COPY public.rol (id, nombre) FROM stdin;
    public          pos    false    241   i�       Z          0    31888    rol_permiso 
   TABLE DATA           9   COPY public.rol_permiso (permiso_id, rol_id) FROM stdin;
    public          pos    false    242   ��       \          0    31894    sucursal 
   TABLE DATA           \   COPY public.sucursal (activo, id, codigo, nit, direccion, nombre, razon_social) FROM stdin;
    public          pos    false    244   ��       ]          0    31901 
   suscriptor 
   TABLE DATA           h   COPY public.suscriptor (estado, fecha_final, fecha_inicio, id, id_plan, id_usuario, nombre) FROM stdin;
    public          pos    false    245   �       ^          0    31908    tarjeta 
   TABLE DATA           j   COPY public.tarjeta (id_suscriptor, "mes_año", cvc, id, nro_tarjeta, correo, nombre_titular) FROM stdin;
    public          pos    false    246   b�       _          0    31919    user_rol 
   TABLE DATA           3   COPY public.user_rol (rol_id, user_id) FROM stdin;
    public          pos    false    247   �       `          0    31924    usuario 
   TABLE DATA           �   COPY public.usuario (activo, credenciales_no_expiradas, cuenta_no_bloqueada, cuenta_no_expirada, id, apellido, email, nombre, password) FROM stdin;
    public          pos    false    248   ��       g           0    0    almacen_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.almacen_seq', 51, true);
          public          pos    false    215            h           0    0 	   cajas_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.cajas_seq', 51, true);
          public          pos    false    216            i           0    0    categoria_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.categoria_seq', 1, true);
          public          pos    false    217            j           0    0    cliente_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.cliente_seq', 1, false);
          public          pos    false    218            k           0    0    detalle_nota_entrada_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.detalle_nota_entrada_seq', 1, false);
          public          pos    false    219            l           0    0    nota_entrada_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.nota_entrada_seq', 1, false);
          public          pos    false    220            m           0    0    permiso_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.permiso_seq', 1, false);
          public          pos    false    221            n           0    0    plan_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.plan_seq', 101, true);
          public          pos    false    222            o           0    0    producto_almacen_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.producto_almacen_seq', 1, false);
          public          pos    false    223            p           0    0    producto_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.producto_seq', 1, true);
          public          pos    false    224            q           0    0    proveedor_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.proveedor_seq', 1, false);
          public          pos    false    225            r           0    0    rol_seq    SEQUENCE SET     5   SELECT pg_catalog.setval('public.rol_seq', 1, true);
          public          pos    false    226            s           0    0    sucursal_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.sucursal_id_seq', 1, true);
          public          pos    false    243            t           0    0    suscriptor_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.suscriptor_seq', 51, true);
          public          pos    false    227            u           0    0    tarjeta_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.tarjeta_seq', 1, false);
          public          pos    false    228            v           0    0    usuario_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.usuario_seq', 251, true);
          public          pos    false    229            n           2606    31816    almacen almacen_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.almacen
    ADD CONSTRAINT almacen_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.almacen DROP CONSTRAINT almacen_pkey;
       public            pos    false    230            p           2606    31821    cajas cajas_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.cajas
    ADD CONSTRAINT cajas_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.cajas DROP CONSTRAINT cajas_pkey;
       public            pos    false    231            r           2606    31830    categoria categoria_nombre_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_nombre_key UNIQUE (nombre);
 H   ALTER TABLE ONLY public.categoria DROP CONSTRAINT categoria_nombre_key;
       public            pos    false    232            t           2606    31828    categoria categoria_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.categoria DROP CONSTRAINT categoria_pkey;
       public            pos    false    232            v           2606    31839    cliente cliente_email_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_email_key UNIQUE (email);
 C   ALTER TABLE ONLY public.cliente DROP CONSTRAINT cliente_email_key;
       public            pos    false    233            x           2606    31837    cliente cliente_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.cliente DROP CONSTRAINT cliente_pkey;
       public            pos    false    233            z           2606    31844 .   detalle_nota_entrada detalle_nota_entrada_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.detalle_nota_entrada
    ADD CONSTRAINT detalle_nota_entrada_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.detalle_nota_entrada DROP CONSTRAINT detalle_nota_entrada_pkey;
       public            pos    false    234            |           2606    31849    nota_entrada nota_entrada_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.nota_entrada
    ADD CONSTRAINT nota_entrada_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.nota_entrada DROP CONSTRAINT nota_entrada_pkey;
       public            pos    false    235            ~           2606    31854    permiso permiso_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.permiso
    ADD CONSTRAINT permiso_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.permiso DROP CONSTRAINT permiso_pkey;
       public            pos    false    236            �           2606    31861    plan plan_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.plan
    ADD CONSTRAINT plan_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.plan DROP CONSTRAINT plan_pkey;
       public            pos    false    237            �           2606    31873 &   producto_almacen producto_almacen_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.producto_almacen
    ADD CONSTRAINT producto_almacen_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.producto_almacen DROP CONSTRAINT producto_almacen_pkey;
       public            pos    false    239            �           2606    31868    producto producto_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.producto DROP CONSTRAINT producto_pkey;
       public            pos    false    238            �           2606    31882    proveedor proveedor_email_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_email_key UNIQUE (email);
 G   ALTER TABLE ONLY public.proveedor DROP CONSTRAINT proveedor_email_key;
       public            pos    false    240            �           2606    31880    proveedor proveedor_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.proveedor DROP CONSTRAINT proveedor_pkey;
       public            pos    false    240            �           2606    31892    rol_permiso rol_permiso_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.rol_permiso
    ADD CONSTRAINT rol_permiso_pkey PRIMARY KEY (permiso_id, rol_id);
 F   ALTER TABLE ONLY public.rol_permiso DROP CONSTRAINT rol_permiso_pkey;
       public            pos    false    242    242            �           2606    31887    rol rol_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.rol DROP CONSTRAINT rol_pkey;
       public            pos    false    241            �           2606    31900    sucursal sucursal_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.sucursal
    ADD CONSTRAINT sucursal_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.sucursal DROP CONSTRAINT sucursal_pkey;
       public            pos    false    244            �           2606    31907 $   suscriptor suscriptor_id_usuario_key 
   CONSTRAINT     e   ALTER TABLE ONLY public.suscriptor
    ADD CONSTRAINT suscriptor_id_usuario_key UNIQUE (id_usuario);
 N   ALTER TABLE ONLY public.suscriptor DROP CONSTRAINT suscriptor_id_usuario_key;
       public            pos    false    245            �           2606    31905    suscriptor suscriptor_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.suscriptor
    ADD CONSTRAINT suscriptor_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.suscriptor DROP CONSTRAINT suscriptor_pkey;
       public            pos    false    245            �           2606    31916 !   tarjeta tarjeta_id_suscriptor_key 
   CONSTRAINT     e   ALTER TABLE ONLY public.tarjeta
    ADD CONSTRAINT tarjeta_id_suscriptor_key UNIQUE (id_suscriptor);
 K   ALTER TABLE ONLY public.tarjeta DROP CONSTRAINT tarjeta_id_suscriptor_key;
       public            pos    false    246            �           2606    31918    tarjeta tarjeta_nro_tarjeta_key 
   CONSTRAINT     a   ALTER TABLE ONLY public.tarjeta
    ADD CONSTRAINT tarjeta_nro_tarjeta_key UNIQUE (nro_tarjeta);
 I   ALTER TABLE ONLY public.tarjeta DROP CONSTRAINT tarjeta_nro_tarjeta_key;
       public            pos    false    246            �           2606    31914    tarjeta tarjeta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.tarjeta
    ADD CONSTRAINT tarjeta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.tarjeta DROP CONSTRAINT tarjeta_pkey;
       public            pos    false    246            �           2606    31923    user_rol user_rol_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.user_rol
    ADD CONSTRAINT user_rol_pkey PRIMARY KEY (rol_id, user_id);
 @   ALTER TABLE ONLY public.user_rol DROP CONSTRAINT user_rol_pkey;
       public            pos    false    247    247            �           2606    31932    usuario usuario_email_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);
 C   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_email_key;
       public            pos    false    248            �           2606    31930    usuario usuario_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_pkey;
       public            pos    false    248            �           2606    32003 #   tarjeta fk15fklffp0m8l50tn2w5x98d4r    FK CONSTRAINT     �   ALTER TABLE ONLY public.tarjeta
    ADD CONSTRAINT fk15fklffp0m8l50tn2w5x98d4r FOREIGN KEY (id_suscriptor) REFERENCES public.suscriptor(id);
 M   ALTER TABLE ONLY public.tarjeta DROP CONSTRAINT fk15fklffp0m8l50tn2w5x98d4r;
       public          pos    false    4754    246    245            �           2606    31993 &   suscriptor fk1u8bbksjt1ton71k8rq46sh6l    FK CONSTRAINT     �   ALTER TABLE ONLY public.suscriptor
    ADD CONSTRAINT fk1u8bbksjt1ton71k8rq46sh6l FOREIGN KEY (id_plan) REFERENCES public.plan(id);
 P   ALTER TABLE ONLY public.suscriptor DROP CONSTRAINT fk1u8bbksjt1ton71k8rq46sh6l;
       public          pos    false    237    4736    245            �           2606    31938 !   cajas fk4njcyi09bq8vcp0y5lcwb2sis    FK CONSTRAINT     �   ALTER TABLE ONLY public.cajas
    ADD CONSTRAINT fk4njcyi09bq8vcp0y5lcwb2sis FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id);
 K   ALTER TABLE ONLY public.cajas DROP CONSTRAINT fk4njcyi09bq8vcp0y5lcwb2sis;
       public          pos    false    231    4750    244            �           2606    31978 %   proveedor fk50f46lehe9mcfghxop7lah0f0    FK CONSTRAINT     �   ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT fk50f46lehe9mcfghxop7lah0f0 FOREIGN KEY (id_almacen) REFERENCES public.almacen(id);
 O   ALTER TABLE ONLY public.proveedor DROP CONSTRAINT fk50f46lehe9mcfghxop7lah0f0;
       public          pos    false    4718    230    240            �           2606    31988 '   rol_permiso fk6o522368i97la9m9cqn0gul2e    FK CONSTRAINT     �   ALTER TABLE ONLY public.rol_permiso
    ADD CONSTRAINT fk6o522368i97la9m9cqn0gul2e FOREIGN KEY (rol_id) REFERENCES public.rol(id);
 Q   ALTER TABLE ONLY public.rol_permiso DROP CONSTRAINT fk6o522368i97la9m9cqn0gul2e;
       public          pos    false    241    242    4746            �           2606    31943 /   detalle_nota_entrada fk928tl6pdys1skel1gkgw1c21    FK CONSTRAINT     �   ALTER TABLE ONLY public.detalle_nota_entrada
    ADD CONSTRAINT fk928tl6pdys1skel1gkgw1c21 FOREIGN KEY (id_nota_entrada) REFERENCES public.nota_entrada(id);
 Y   ALTER TABLE ONLY public.detalle_nota_entrada DROP CONSTRAINT fk928tl6pdys1skel1gkgw1c21;
       public          pos    false    234    4732    235            �           2606    31933 #   almacen fk9mrcrpiho9erw6yk1rtpwhgk4    FK CONSTRAINT     �   ALTER TABLE ONLY public.almacen
    ADD CONSTRAINT fk9mrcrpiho9erw6yk1rtpwhgk4 FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id);
 M   ALTER TABLE ONLY public.almacen DROP CONSTRAINT fk9mrcrpiho9erw6yk1rtpwhgk4;
       public          pos    false    4750    244    230            �           2606    31963 #   producto fk9nyueixdsgbycfhf7allg8su    FK CONSTRAINT     �   ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk9nyueixdsgbycfhf7allg8su FOREIGN KEY (id_categoria) REFERENCES public.categoria(id);
 M   ALTER TABLE ONLY public.producto DROP CONSTRAINT fk9nyueixdsgbycfhf7allg8su;
       public          pos    false    238    4724    232            �           2606    31968 ,   producto_almacen fkcj3r338p3pqrfqm6p6k3ndsxh    FK CONSTRAINT     �   ALTER TABLE ONLY public.producto_almacen
    ADD CONSTRAINT fkcj3r338p3pqrfqm6p6k3ndsxh FOREIGN KEY (almacen_id) REFERENCES public.almacen(id);
 V   ALTER TABLE ONLY public.producto_almacen DROP CONSTRAINT fkcj3r338p3pqrfqm6p6k3ndsxh;
       public          pos    false    239    4718    230            �           2606    31948 0   detalle_nota_entrada fkfwwc3t11n02rjtbmw7ce5o5ie    FK CONSTRAINT     �   ALTER TABLE ONLY public.detalle_nota_entrada
    ADD CONSTRAINT fkfwwc3t11n02rjtbmw7ce5o5ie FOREIGN KEY (producto_id) REFERENCES public.producto(id);
 Z   ALTER TABLE ONLY public.detalle_nota_entrada DROP CONSTRAINT fkfwwc3t11n02rjtbmw7ce5o5ie;
       public          pos    false    234    4738    238            �           2606    31983 '   rol_permiso fkfyao8wd0o5tsyem1w55s3141k    FK CONSTRAINT     �   ALTER TABLE ONLY public.rol_permiso
    ADD CONSTRAINT fkfyao8wd0o5tsyem1w55s3141k FOREIGN KEY (permiso_id) REFERENCES public.permiso(id);
 Q   ALTER TABLE ONLY public.rol_permiso DROP CONSTRAINT fkfyao8wd0o5tsyem1w55s3141k;
       public          pos    false    236    242    4734            �           2606    32013 $   user_rol fkggmqnaahwf98dagwnykujimtt    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_rol
    ADD CONSTRAINT fkggmqnaahwf98dagwnykujimtt FOREIGN KEY (user_id) REFERENCES public.usuario(id);
 N   ALTER TABLE ONLY public.user_rol DROP CONSTRAINT fkggmqnaahwf98dagwnykujimtt;
       public          pos    false    247    4766    248            �           2606    31998 &   suscriptor fkhg6mk87c2pcd7ctp3gfe6fbw2    FK CONSTRAINT     �   ALTER TABLE ONLY public.suscriptor
    ADD CONSTRAINT fkhg6mk87c2pcd7ctp3gfe6fbw2 FOREIGN KEY (id_usuario) REFERENCES public.usuario(id);
 P   ALTER TABLE ONLY public.suscriptor DROP CONSTRAINT fkhg6mk87c2pcd7ctp3gfe6fbw2;
       public          pos    false    245    248    4766            �           2606    31958 (   nota_entrada fkm0t15b4teip45m7j92a71e012    FK CONSTRAINT     �   ALTER TABLE ONLY public.nota_entrada
    ADD CONSTRAINT fkm0t15b4teip45m7j92a71e012 FOREIGN KEY (id_proveedor) REFERENCES public.proveedor(id);
 R   ALTER TABLE ONLY public.nota_entrada DROP CONSTRAINT fkm0t15b4teip45m7j92a71e012;
       public          pos    false    235    4744    240            �           2606    31953 (   nota_entrada fkm45tr4g598iq8te5cqh8btxro    FK CONSTRAINT     �   ALTER TABLE ONLY public.nota_entrada
    ADD CONSTRAINT fkm45tr4g598iq8te5cqh8btxro FOREIGN KEY (id_almacen) REFERENCES public.almacen(id);
 R   ALTER TABLE ONLY public.nota_entrada DROP CONSTRAINT fkm45tr4g598iq8te5cqh8btxro;
       public          pos    false    235    4718    230            �           2606    32008 #   user_rol fkpfraq7jod5w5xd3sxm3m6y1o    FK CONSTRAINT        ALTER TABLE ONLY public.user_rol
    ADD CONSTRAINT fkpfraq7jod5w5xd3sxm3m6y1o FOREIGN KEY (rol_id) REFERENCES public.rol(id);
 M   ALTER TABLE ONLY public.user_rol DROP CONSTRAINT fkpfraq7jod5w5xd3sxm3m6y1o;
       public          pos    false    4746    241    247            �           2606    31973 ,   producto_almacen fks5c1n0xo5jopuqdtauh4epl8a    FK CONSTRAINT     �   ALTER TABLE ONLY public.producto_almacen
    ADD CONSTRAINT fks5c1n0xo5jopuqdtauh4epl8a FOREIGN KEY (producto_id) REFERENCES public.producto(id);
 V   ALTER TABLE ONLY public.producto_almacen DROP CONSTRAINT fks5c1n0xo5jopuqdtauh4epl8a;
       public          pos    false    4738    238    239            N   ;   x��1�0��~R��ƂXK*��d�[�0���Ʌ����7w�|
�3�����C�      O   #   x�K�4�Ĝ����<C�N# ���+F��� k�      P      x�+�4�LO,N�/N,�3�b���� z      Q      x������ � �      R      x������ � �      S      x������ � �      T      x������ � �      U   �   x����n� �y�<@'�q�v]Жä�H�<؞�/V��u����|��l]�3��;NK=�K���>qW������<w��A1���B��	}0 a=Z��K̼#\	<�9�^c�V��!��Q;n`w�|�Ґ|��!�A��`�rR��l��b��:�7�^m�a)�%��ڻ��Va�
�?��VḜs���؆� ������F�}uWr���5���ԭ�so��{���      V   ,   x�+�44�44�4Bc#C#C����D����D�@��+F��� ��      W      x������ � �      X      x������ � �      Y      x�3�tt�������� �V      Z      x������ � �      \   K   x�+�4�4525�42455155�,K�+I,VHIUHN,�K-VH�Q(�/R�M���
��y@�3)-�I%W� �#�      ]   L   x�+�4202�54�5��2t�8A�Ԉӫ41��$c
�ARd�ij�s��f�':��&f��%��r��qqq x��      ^      x������ � �      _      x�3�4�2�4bC#i
"���=... A�"      `   �  x�U��r�0��ux�)	q�Q+V�L7R��	x��N�e�9s��?hn�1��bց��a�qWҴ�Q]�LHd�0[�AUXF��j}�ъ��?W��K
'y�f�z
��n�S���4�lӚ�(@8�Gi$j�G�Q���[�<�fS32H�
�4c�$��7������s���P(M�[Y�����Z������f�ݝ:h�����-�7vF�Ŭ�W���ec*�,{	�]�~
��,�a��x����_�Gv��`7���;�A�U9�aP�K��ys�F�R�7�B�)���3�06b�~(�W�U����OU_��!�N͜�{�陳�ٟ��)�R��Ow��_�49o>�AS�-��O]���$}�-��     