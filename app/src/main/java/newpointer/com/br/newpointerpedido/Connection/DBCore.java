package newpointer.com.br.newpointerpedido.Connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FelipeRsN on 6/24/16.
 */
public class DBCore extends SQLiteOpenHelper {
    private static final String DB_NAME = "PedidoNewPointer";
    private static final int DB_VERSION = 18;

    public DBCore(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table config(db_string text, estacao text, taxa real, digito_verificador integer, pergunta_mesa integer, titulo_loja text, nmin_mesa text, nmax_mesa text, dbbkp_date text, phone_selection integer, product_selection integer);");
        db.execSQL("create table product(id text, name text, family integer, unit text, fl_imp integer, cd_imp integer, fl_acomp integer, acomp text, atalho integer);");
        db.execSQL("create table family(id integer, name text);");
        db.execSQL("create table groupacomp(id integer, name text, selecao integer);");
        db.execSQL("create table acomp(id integer, name text, grupo integer);");
        db.execSQL("create table carrinho(id_carrinho integer primary key autoincrement not null, id_prod text, name_prod text, qtd_prod integer, acomp_prod text, obs_prod text);");
        db.execSQL("create table operador(id_usu integer, name text, password text, fl_iniciar integer, fl_primeiro_acesso integer, fl_perfil integer,  cd_perfil integer);");
        db.execSQL("create table operador_funcao(cd_operador integer, cd_funcao text)");
        db.execSQL("create table perfil_funcao(cd_perfil integer, cd_funcao text)");
        db.execSQL("create table current_op(cd_operador integer, nm_operador text)");
        db.execSQL("create table prod_associado(id_associado text, id_prod text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table IF EXISTS config;");
        db.execSQL("drop table IF EXISTS product;");
        db.execSQL("drop table IF EXISTS family;");
        db.execSQL("drop table IF EXISTS groupacomp;");
        db.execSQL("drop table IF EXISTS acomp;");
        db.execSQL("drop table IF EXISTS carrinho;");
        db.execSQL("drop table IF EXISTS operador;");
        db.execSQL("drop table IF EXISTS operador_funcao;");
        db.execSQL("drop table IF EXISTS perfil_funcao;");
        db.execSQL("drop table IF EXISTS current_op");
        db.execSQL("drop table IF EXISTS prod_associado");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table IF EXISTS config;");
        db.execSQL("drop table IF EXISTS product;");
        db.execSQL("drop table IF EXISTS family;");
        db.execSQL("drop table IF EXISTS groupacomp;");
        db.execSQL("drop table IF EXISTS acomp;");
        db.execSQL("drop table IF EXISTS carrinho;");
        db.execSQL("drop table IF EXISTS operador;");
        db.execSQL("drop table IF EXISTS operador_funcao;");
        db.execSQL("drop table IF EXISTS perfil_funcao;");
        db.execSQL("drop table IF EXISTS current_op");
        db.execSQL("drop table IF EXISTS prod_associado");
        onCreate(db);
    }
}
