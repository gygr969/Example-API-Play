# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tarea_bd (
  id                            bigint not null,
  tarea                         varchar(255),
  tipo                          varchar(255),
  usuario_id                    bigint,
  constraint pk_tarea_bd primary key (id)
);
create sequence tarea_bd_seq;

create table usuario_bd (
  id                            bigint not null,
  nombre                        varchar(255),
  constraint pk_usuario_bd primary key (id)
);
create sequence usuario_bd_seq;

alter table tarea_bd add constraint fk_tarea_bd_usuario_id foreign key (usuario_id) references usuario_bd (id) on delete restrict on update restrict;
create index ix_tarea_bd_usuario_id on tarea_bd (usuario_id);


# --- !Downs

alter table tarea_bd drop constraint if exists fk_tarea_bd_usuario_id;
drop index if exists ix_tarea_bd_usuario_id;

drop table if exists tarea_bd;
drop sequence if exists tarea_bd_seq;

drop table if exists usuario_bd;
drop sequence if exists usuario_bd_seq;

