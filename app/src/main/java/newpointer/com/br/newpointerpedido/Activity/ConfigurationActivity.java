package newpointer.com.br.newpointerpedido.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection;
import newpointer.com.br.newpointerpedido.Model.ConfigModel;
import newpointer.com.br.newpointerpedido.R;

public class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText string_bd;
    private EditText taxa;
    private EditText estacao;
    private Switch digito_verificador;
    private Switch pergunta_mesa;
    private Button atualizar;
    private ImageButton ib_return;
    private DBLiteConnection dbl;
    private String bdconexao = "";
    private double bdtaxa = 0.0;
    private String bdestacao = "";
    private int bdmesa = 0;
    private int bddigito = 0;
    private int backupmesa = 0;
    private int backupdigito = 0;
    private Spinner busca;
    private Spinner modo_op;
    private String backupestacao = "";
    private String backupstring = "";
    private double backuptaxa = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        startVar();

        List<String> spinnerModo =  new ArrayList<String>();
        spinnerModo.add("Celular");
        spinnerModo.add("Tablet");

        ArrayAdapter<String> adapterModo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerModo);
        adapterModo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        modo_op.setAdapter(adapterModo);

        List<String> spinnerBusca =  new ArrayList<String>();
        spinnerBusca.add("Atalhos");
        spinnerBusca.add("Digitar código");
        spinnerBusca.add("Famílias");

        ArrayAdapter<String> adapterBusca = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerBusca);
        adapterModo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        busca.setAdapter(adapterBusca);

        ConfigModel cfg = dbl.selectConfig();
        string_bd.setText(cfg.getString_bd().toString());
        estacao.setText(cfg.getEstacao().toString());
        taxa.setText(cfg.getTaxa().toString()+"");
        if(cfg.getDigito_verificador() == 1)digito_verificador.setChecked(true);
        else digito_verificador.setChecked(false);
        if(cfg.getPergunta_mesa() == 1)pergunta_mesa.setChecked(true);
        else pergunta_mesa.setChecked(false);

        busca.setSelection(cfg.getProduct_selection());

        modo_op.setSelection(cfg.getPhone_selection());

        backupdigito = cfg.getDigito_verificador();
        backupestacao = cfg.getEstacao();
        backupmesa = cfg.getPergunta_mesa();
        backupstring = cfg.getString_bd();
        backuptaxa = cfg.getTaxa();
    }

    private void startVar(){
        string_bd = (EditText) findViewById(R.id.et_conf_string);
        taxa = (EditText) findViewById(R.id.et_conf_taxa);
        estacao = (EditText) findViewById(R.id.et_conf_estacao);
        digito_verificador = (Switch) findViewById(R.id.sw_conf_digito);
        pergunta_mesa = (Switch) findViewById(R.id.sw_conf_mesa);
        atualizar = (Button) findViewById(R.id.bt_conf_atualizar);
        ib_return = (ImageButton) findViewById(R.id.ib_conf_return);
        busca = (Spinner) findViewById(R.id.sp_conf_prod);
        modo_op = (Spinner) findViewById(R.id.sp_conf_operacao);
        dbl = new DBLiteConnection(ConfigurationActivity.this);

        atualizar.setOnClickListener(this);
        ib_return.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        int p = 0, d = 0;
        if(pergunta_mesa.isChecked()) p = 1;
        if(digito_verificador.isChecked()) d = 1;
        if(!backupstring.equalsIgnoreCase(string_bd.getText().toString()) || !backupestacao.equalsIgnoreCase(estacao.getText().toString()) || backuptaxa != Double.parseDouble(taxa.getText().toString()) || backupmesa != p || backupdigito != d){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Sair das configurações");
            builder.setMessage("Alguns itens foram modificados e ainda não foram salvos, deseja excluir as modificações e sair?");
            builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        }else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == ib_return){
            int p = 0, d = 0;
            if(pergunta_mesa.isChecked()) p = 1;
            if(digito_verificador.isChecked()) d = 1;
            if(!backupstring.equalsIgnoreCase(string_bd.getText().toString()) || !backupestacao.equalsIgnoreCase(estacao.getText().toString()) || backuptaxa != Double.parseDouble(taxa.getText().toString()) || backupmesa != p || backupdigito != d){
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
                builder.setTitle("Sair das configurações");
                builder.setMessage("Alguns itens foram modificados e ainda não foram salvos, deseja excluir as modificações e sair?");
                builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.setCancelable(false);
                builder.show();
            }else{
                finish();
            }
        }
        if(v == atualizar){
            if(string_bd.getText().toString().length() > 16) {
                if (estacao.getText().toString().length() > 3) {
                    if (taxa.getText().toString().length() > 2) {
                        bdconexao = string_bd.getText().toString();
                        bdestacao = estacao.getText().toString();
                        bdtaxa = Double.parseDouble(taxa.getText().toString());
                        String data = dbl.selectConfig().getDbbkp_date();
                        String nMin = dbl.selectConfig().getNmin_mesa();
                        String nMax = dbl.selectConfig().getNmax_mesa();
                        String title = dbl.selectConfig().getTitulo_loja();
                        if(pergunta_mesa.isChecked()) bdmesa = 1;
                        if(digito_verificador.isChecked()) bddigito = 1;
                        int selecBusca = busca.getSelectedItemPosition();
                        int selecModo = modo_op.getSelectedItemPosition();
                        dbl.deleteConfig();
                        dbl.insertConfig(bdconexao,bdestacao,bdtaxa,bddigito,bdmesa,title,nMin,nMax, data, selecModo, selecBusca);
                        Toast.makeText(ConfigurationActivity.this, "Dados de configuração atualizados com sucesso. Reiniciando sistema...", Toast.LENGTH_SHORT).show();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else{
                        taxa.setError("Campo obrigatório, caso não use taxa digite 0.0");
                    }
                }else{
                    estacao.setError("Campo obrigatório para identificação do dispositivo e operador");
                }
            }else{
                string_bd.setError("Campo obrigatório para conexão");
            }
        }
    }
}
