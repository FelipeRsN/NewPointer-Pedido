package newpointer.com.br.newpointerpedido.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_configuration.*
import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection
import newpointer.com.br.newpointerpedido.Model.ConfigModel
import newpointer.com.br.newpointerpedido.R


class ConfigurationActivity : AppCompatActivity() {

    private lateinit var configvalue: ConfigModel
    private lateinit var dbl: DBLiteConnection

    private var backupDigito = false
    private var backupMesa = false
    private var backupPreconta = false
    private var backupConferencia = false
    private var backupBusca = 0
    private var backupOperacao = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        setAdapters()
        getConfigData()
        fillFields()
        setupListeners()
    }

    private fun setAdapters() {
        val spinnerModo = ArrayList<String>()
        spinnerModo.add("Celular")
        spinnerModo.add("Tablet")
        val adapterModo = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerModo)
        adapterModo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_conf_modo_operacao.adapter = adapterModo

        val spinnerBusca = ArrayList<String>()
        spinnerBusca.add("Atalhos")
        spinnerBusca.add("Digitar código")
        spinnerBusca.add("Famílias")
        val adapterBusca = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerBusca)
        adapterModo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_conf_busca.adapter = adapterBusca
    }

    private fun getConfigData() {
        dbl = DBLiteConnection(this)
        configvalue = dbl.selectConfig()
    }

    private fun fillFields() {
        et_conf_string.setText(configvalue.string_bd)
        et_conf_estacao.setText(configvalue.estacao)
        et_conf_taxa.setText(configvalue.taxa.toString())

        backupDigito = configvalue.digito_verificador == 1
        sw_conf_digito.isChecked = backupDigito

        backupMesa = configvalue.pergunta_mesa == 1
        sw_conf_mesa.isChecked = backupMesa

        backupBusca = configvalue.busca_selection
        sp_conf_busca.setSelection(backupBusca)

        backupOperacao = configvalue.operacao_selection
        sp_conf_modo_operacao.setSelection(backupOperacao)

        backupPreconta = configvalue.preconta == 1
        sw_conf_preconta.isChecked = backupPreconta

        backupConferencia = configvalue.conferencia == 1
        sw_conf_conferencia.isChecked = backupConferencia
    }

    private fun setupListeners() {
        ib_conf_return.setOnClickListener {
            onBackPressed()
        }

        bt_conf_atualizar.setOnClickListener {
            et_conf_taxa.error = null
            et_conf_string.error = null
            et_conf_estacao.error = null

            if (et_conf_string.length() > 16) {
                if (et_conf_estacao.length() > 3) {
                    if (et_conf_taxa.length() > 2) {

                        val mesa = if (sw_conf_mesa.isChecked) 1 else 0
                        val digito = if (sw_conf_digito.isChecked) 1 else 0
                        val preconta = if (sw_conf_preconta.isChecked) 1 else 0
                        val conferencia = if (sw_conf_conferencia.isChecked) 1 else 0

                        dbl.deleteConfig()
                        dbl.insertConfig(
                                et_conf_string.text.toString(),
                                et_conf_estacao.text.toString(),
                                et_conf_taxa.text.toString().toDouble(),
                                digito,
                                mesa,
                                configvalue.titulo_loja,
                                configvalue.nmin_mesa,
                                configvalue.nmax_mesa,
                                configvalue.dbbkp_date,
                                sp_conf_busca.selectedItemPosition,
                                sp_conf_modo_operacao.selectedItemPosition,
                                preconta,
                                conferencia)

                        Toast.makeText(this, "Dados de configuração atualizados com sucesso. Reiniciando sistema...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, LoadingActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        android.os.Process.killProcess(android.os.Process.myPid())
                        System.exit(0)

                    } else et_conf_taxa.error = "Campo obrigatório, caso não use taxa digite 0.0"
                } else et_conf_estacao.error = "Campo obrigatório para identificação do dispositivo e operador"
            } else et_conf_string.error = "Campo obrigatório para conexão"
        }
    }

    override fun onBackPressed() {
        if (et_conf_string.text.toString() != configvalue.string_bd ||
                et_conf_estacao.text.toString() != configvalue.estacao ||
                et_conf_taxa.text.toString().toDouble() != configvalue.taxa ||
                sw_conf_digito.isChecked != backupDigito ||
                sw_conf_mesa.isChecked != backupMesa ||
                sp_conf_modo_operacao.selectedItem != backupOperacao ||
                sp_conf_busca.selectedItem != backupBusca ||
                sw_conf_preconta.isChecked != backupPreconta ||
                sw_conf_conferencia.isChecked != backupConferencia) {

            val builder = AlertDialog.Builder(this, R.style.YourDialogStyle)
            builder.setTitle("Sair das configurações")
            builder.setMessage("Alguns itens foram modificados e ainda não foram salvos, deseja excluir as modificações e sair?")
            builder.setPositiveButton("Excluir") { dialog, which -> finishAfterTransition() }
            builder.setNegativeButton("Cancelar", null)
            builder.setCancelable(false)
            builder.show()

        } else {
            finishAfterTransition()
        }
    }
}
