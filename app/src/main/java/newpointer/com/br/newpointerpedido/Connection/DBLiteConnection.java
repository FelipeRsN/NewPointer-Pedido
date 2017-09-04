package newpointer.com.br.newpointerpedido.Connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import newpointer.com.br.newpointerpedido.Model.AcompanhamentoModel;
import newpointer.com.br.newpointerpedido.Model.CarrinhoModel;
import newpointer.com.br.newpointerpedido.Model.ConfigModel;
import newpointer.com.br.newpointerpedido.Model.CurrentOperadorModel;
import newpointer.com.br.newpointerpedido.Model.FamilyModel;
import newpointer.com.br.newpointerpedido.Model.GrupoAcompModel;
import newpointer.com.br.newpointerpedido.Model.OperadorModel;
import newpointer.com.br.newpointerpedido.Model.ProductModel;

/**
 * Created by FelipeRsN on 6/24/16.
 */
public class DBLiteConnection{
    private final SQLiteDatabase db;

    public DBLiteConnection(Context ctx){
        DBCore dbcore = new DBCore(ctx);
        db = dbcore.getWritableDatabase();
    }

    /////////////////////// Configuracoes /////////////////////////
    ////// insert /////////

    public void insertConfig(String string_bd, String estacao, Double taxa, int digito_verificador, int pergunta_mesa, String titulo_loja, String nmin_mesa, String nmax_mesa, String bdbkp_date, int phone_selec, int product_selec) {
        ContentValues values = new ContentValues();
        values.put("db_string", string_bd);
        values.put("estacao", estacao);
        values.put("taxa", taxa);
        values.put("digito_verificador", digito_verificador);
        values.put("pergunta_mesa", pergunta_mesa);
        values.put("titulo_loja", titulo_loja);
        values.put("nmin_mesa", nmin_mesa);
        values.put("nmax_mesa", nmax_mesa);
        values.put("dbbkp_date", bdbkp_date);
        values.put("phone_selection", phone_selec);
        values.put("product_selection", product_selec);
        db.insert("config", null, values);
    }

    ////// Delete /////////

    public void deleteConfig() {
        db.delete("config",null, null);
    }

    ////// Select /////////

    public ConfigModel selectConfig() {
        String[] columns = new String[]{"db_string", "estacao", "taxa", "digito_verificador", "pergunta_mesa", "titulo_loja", "nmin_mesa", "nmax_mesa","dbbkp_date","phone_selection","product_selection"};
        Cursor cursor = db.query("config", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ConfigModel conf = new ConfigModel(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10));
                return conf;
            }while(cursor.moveToNext());
        }
        return null;
    }

    public boolean isConfigurated() {
        String[] columns = new String[]{"db_string"};
        Cursor cursor = db.query("config", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            }while(cursor.moveToNext());
        }
        return false;
    }

    /////////////////////// Produto /////////////////////////
    ////// insert /////////

    public void insertProd(String id, String name, int fam, String unit, int fl_imp, int cd_imp, int fl_acomp, String acomp, int atalho) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("family", fam);
        values.put("unit", unit);
        values.put("fl_imp", fl_imp);
        values.put("cd_imp", cd_imp);
        values.put("fl_acomp", fl_acomp);
        values.put("acomp", acomp);
        values.put("atalho", atalho);
        db.insert("product", null, values);
    }

    ////// Delete /////////

    public void deleteAllProd() {
        db.delete("product",null, null);
    }

    ////// Select /////////

    public ArrayList<ProductModel> selectAllProd() {
        ArrayList<ProductModel> prod = new ArrayList<ProductModel>();
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                prod.add(new ProductModel(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5), cursor.getInt(6),cursor.getString(7)));
            }while(cursor.moveToNext());
        }
        return prod;
    }

    public boolean haveProd() {
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public ArrayList<ProductModel> selectProdByFam(int codefam) {
        ArrayList<ProductModel> prod = new ArrayList<ProductModel>();
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "family = "+codefam, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                prod.add(new ProductModel(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5), cursor.getInt(6),cursor.getString(7)));
            }while(cursor.moveToNext());
        }
        return prod;
    }

    public ArrayList<ProductModel> selectProdByName(String name) {
        ArrayList<ProductModel> prod = new ArrayList<ProductModel>();
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "name LIKE '%"+name+"%'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                prod.add(new ProductModel(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5), cursor.getInt(6),cursor.getString(7)));
            }while(cursor.moveToNext());
        }
        return prod;
    }

    public boolean foundSelectProdByName(String name) {
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "name LIKE '%"+name+"%'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            }while(cursor.moveToNext());
        }
        return false;
    }

    public boolean foundSelectProdAtalhos() {
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "atalho = 1", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            }while(cursor.moveToNext());
        }
        return false;
    }

    public ArrayList<ProductModel> selectProdByAtalho() {
        ArrayList<ProductModel> prod = new ArrayList<ProductModel>();
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "atalho = 1", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                prod.add(new ProductModel(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5), cursor.getInt(6),cursor.getString(7)));
            }while(cursor.moveToNext());
        }
        return prod;
    }

    public ProductModel getProdByCode(String code) {
        ProductModel prod = new ProductModel("0","",0,"",0,0,0,"");
        String[] columns = new String[]{"id", "name", "family", "unit", "fl_imp", "cd_imp", "fl_acomp", "acomp"};
        Cursor cursor = db.query("product", columns, "id = '"+code+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                prod.setId(cursor.getString(0));
                prod.setName(cursor.getString(1));
                prod.setFam(cursor.getInt(2));
                prod.setUnit(cursor.getString(3));
                prod.setFl_imp(cursor.getInt(4));
                prod.setCd_imp(cursor.getInt(5));
                prod.setFl_acomp(cursor.getInt(6));
                prod.setAcomp(cursor.getString(7));
            }while(cursor.moveToNext());
        }
        return prod;
    }

    /////////////////////// Familia  /////////////////////////
    ////// insert /////////

    public void insertFam(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        db.insert("family", null, values);
    }

    ////// Delete /////////

    public void deleteAllFam() {
        db.delete("family",null, null);
    }

    ////// Select /////////

    public ArrayList<FamilyModel> selectAllFam() {
        ArrayList<FamilyModel> fam = new ArrayList<FamilyModel>();
        String[] columns = new String[]{"id", "name"};
        Cursor cursor = db.query("family", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                fam.add(new FamilyModel(cursor.getInt(0),cursor.getString(1)));
            }while(cursor.moveToNext());
        }
        return fam;
    }

    public boolean haveFam() {
        ArrayList<FamilyModel> fam = new ArrayList<FamilyModel>();
        String[] columns = new String[]{"id", "name"};
        Cursor cursor = db.query("family", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    /////////////////////// Acomp  /////////////////////////
    ////// insert /////////

    public void insertAcomp(int id, String name, int grupo) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("grupo", grupo);
        db.insert("acomp", null, values);
    }

    ////// Delete /////////

    public void deleteAllAcomp() {
        db.delete("acomp",null, null);
    }

    ////// Select /////////

    public ArrayList<AcompanhamentoModel> selectAcompByGroup(int group) {
        ArrayList<AcompanhamentoModel> aco = new ArrayList<AcompanhamentoModel>();
        String[] columns = new String[]{"id", "name","grupo"};
        Cursor cursor = db.query("acomp", columns, "grupo = "+group, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                aco.add(new AcompanhamentoModel(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
            }while(cursor.moveToNext());
        }
        return aco;
    }

    /////////////////////// Grupo Acomp  /////////////////////////
    ////// insert /////////

    public void insertGrupoAcomp(int id, String name, int selecao) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("selecao", selecao);
        db.insert("groupacomp", null, values);
    }

    ////// Delete /////////

    public void deleteAllGrupoAcomp() {
        db.delete("groupacomp",null, null);
    }

    ////// Select /////////

    public GrupoAcompModel selectGrupoAcomp(int id) {
        GrupoAcompModel group = new GrupoAcompModel(0,"",0);
        String[] columns = new String[]{"id", "name", "selecao"};
        Cursor cursor = db.query("groupacomp", columns, "id = "+id, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                group.setId(cursor.getInt(0));
                group.setName(cursor.getString(1));
                group.setSelecao(cursor.getInt(2));
            }while(cursor.moveToNext());
        }
        return group;
    }

    /////////////////////// Carrinho /////////////////////////
    ////// insert /////////

    public void insertProdCarrinho(String id, String name, int qtd, String acomp, String obs) {
        ContentValues values = new ContentValues();
        values.put("id_prod", id);
        values.put("name_prod", name);
        values.put("qtd_prod", qtd);
        values.put("acomp_prod", acomp);
        values.put("obs_prod", obs);
        db.insert("carrinho", null, values);
    }

    ////// Delete /////////

    public void deleteAllCarrinho() {
        db.delete("carrinho",null, null);
    }

    public void deleteProdCarrinho(int id) {
        db.delete("carrinho","id_carrinho = "+id, null);
    }

    ////// Select /////////

    public ArrayList<CarrinhoModel> selectCarrinho() {
        ArrayList<CarrinhoModel> carrinho = new ArrayList<CarrinhoModel>();
        String[] columns = new String[]{"id_carrinho", "id_prod","name_prod","qtd_prod","acomp_prod","obs_prod"};
        Cursor cursor = db.query("carrinho", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                carrinho.add(new CarrinhoModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return carrinho;
    }

    public boolean haveProdInCarrinho() {
        String[] columns = new String[]{"id_carrinho"};
        Cursor cursor = db.query("carrinho", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            }while(cursor.moveToNext());
        }
        return false;
    }

    /////////////////////// Operador /////////////////////////
    ////// insert /////////

    public void insertOp(int id, String name, int fliniciar, int flprimeiro, String pass, int flperfil, int cdperfil) {
        ContentValues values = new ContentValues();
        values.put("id_usu", id);
        values.put("name", name);
        values.put("fl_iniciar", fliniciar);
        values.put("fl_primeiro_acesso", flprimeiro);
        values.put("password", pass);
        values.put("fl_perfil", flperfil);
        values.put("cd_perfil", cdperfil);
        db.insert("operador", null, values);
    }

    public void insertOpFunc(int cd, String func) {
        ContentValues values = new ContentValues();
        values.put("cd_operador", cd);
        values.put("cd_funcao", func);
        db.insert("operador_funcao", null, values);
    }

    public void insertPerfilFuncao(int cd, String func) {
        ContentValues values = new ContentValues();
        values.put("cd_perfil", cd);
        values.put("cd_funcao", func);
        db.insert("perfil_funcao", null, values);
    }

    ////// Delete /////////

    public void deleteAllOP() {
        db.delete("operador",null, null);
    }

    public void deleteAllOPFunc() {
        db.delete("operador_funcao",null, null);
    }

    public void deleteAllPerfilFunc() {
        db.delete("perfil_funcao",null, null);
    }

    ////// Select /////////

    public OperadorModel selectOp(int cd) {
        OperadorModel op;
    String[] columns = new String[]{"id_usu","name","fl_iniciar","fl_primeiro_acesso","password","fl_perfil","cd_perfil"};
        Cursor cursor = db.query("operador", columns, "id_usu = "+cd, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                op = new OperadorModel(cursor.getInt(0),cursor.getString(1),cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
                return op;
            }while(cursor.moveToNext());
        }
        return null;
    }

    public String selectOpFunc(int cd) {
        String result = "";
        String[] columns = new String[]{"cd_funcao"};
        Cursor cursor = db.query("operador_funcao", columns, "cd_operador = "+cd, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return result;
    }

    public String selectPerfilFunc(int cd) {
        String result = "";
        String[] columns = new String[]{"cd_funcao"};
        Cursor cursor = db.query("perfil_funcao", columns, "cd_perfil = "+cd, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return result;
    }

    /////////////////////// Current_Operador /////////////////////////
    ////// insert /////////

    public void insertCurrentOp(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("cd_operador", id);
        values.put("nm_operador", name);
        db.insert("current_op", null, values);
    }

    ////// Delete /////////

    public void deleteCurrentOP() {
        db.delete("current_op",null, null);
    }


    ////// Select /////////

    public CurrentOperadorModel selectCurrentOp() {
        CurrentOperadorModel op;
        String[] columns = new String[]{"cd_operador","nm_operador"};
        Cursor cursor = db.query("current_op", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                op = new CurrentOperadorModel(cursor.getInt(0),cursor.getString(1));
                return op;
            }while(cursor.moveToNext());
        }
        return null;
    }

    /////////////////////// Associados /////////////////////////
    ////// insert /////////

    public void insertAssociados(String id, String id_prod) {
        ContentValues values = new ContentValues();
        values.put("id_associado", id);
        values.put("id_prod", id_prod);
        db.insert("prod_associado", null, values);
    }

    ////// Delete /////////

    public void deleteAssociados() {
        db.delete("prod_associado",null, null);
    }


    ////// Select /////////

    public ProductModel getProdByIdAssociado(String id_associado) {
        Log.i("GETPRODBYID", "Entrou no getByAssociados com o id"+id_associado);
        String[] columns = new String[]{"id_prod"};
        Cursor cursor = db.query("prod_associado", columns, "id_associado = '"+id_associado+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.i("GETPRODBYID", "Achou um associado e retornou o codigo de prod "+cursor.getString(0));
                return getProdByCode(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        Log.i("GETPRODBYID", "Nao achou o associado");
        return new ProductModel("0","",0,"",0,0,0,"");
    }

    public ProductModel getProdByAssociadoAndCode(String id){
        Log.i("GETPRODBYID", "Entrou com ID: "+id);
        ProductModel p = getProdByCode(id);
        if (p.getName().equalsIgnoreCase("")) {
            Log.i("GETPRODBYID", "nao encontrou o prod "+id+" na tabela prod");
            return getProdByIdAssociado(id);
        }else{
            Log.i("GETPRODBYID", "Encontrou o prod na tabela prod e ta retornando");
            return p;
        }
    }

    public void getAllAssociados(){
        String[] columns = new String[]{"id_associado"};
        Cursor cursor = db.query("prod_associado", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.i("ASSOCIADOS", "getAllAssociados: "+cursor.getString(0));
            }while(cursor.moveToNext());
        }
    }


}
