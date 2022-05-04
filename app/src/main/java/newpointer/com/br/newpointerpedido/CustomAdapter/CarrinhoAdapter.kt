package newpointer.com.br.newpointerpedido.CustomAdapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import newpointer.com.br.newpointerpedido.Activity.CarinhoActivity
import newpointer.com.br.newpointerpedido.Activity.MainActivity.badge
import newpointer.com.br.newpointerpedido.Connection.DBLiteConnection
import newpointer.com.br.newpointerpedido.Model.CarrinhoModel
import newpointer.com.br.newpointerpedido.R

class CarrinhoAdapter(private val context: Context, private val activity: Activity, private val listCars: List<CarrinhoModel>, private val pergMesa: Boolean, private val titulo: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentComanda = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = CarrinhoViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_carrinho, parent, false))

        viewHolder.del?.setOnClickListener {

            val item = listCars[viewHolder.adapterPosition]

            val itens = activity.findViewById(R.id.tv_carrinho_nitens) as TextView

            val dbl = DBLiteConnection(context)
            dbl.deleteProdCarrinho(listCars[viewHolder.adapterPosition].id_carrinho)

            CarinhoActivity.carrinho.removeAt(viewHolder.adapterPosition)

            this.notifyItemRemoved(viewHolder.adapterPosition)

            if (item.firstItem != null && item.firstItem) {
                run outForeach@{
                    listCars.forEachIndexed { index, carrinhoModel ->
                        if (carrinhoModel.id_carrinho != item.id_carrinho && carrinhoModel.numComanda == item.numComanda) {
                            carrinhoModel.firstItem = true
                            notifyItemChanged(index)
                            return@outForeach
                        }
                    }
                }
            }

            var b = Integer.parseInt(badge.text.toString())
            b--
            badge.text = "" + b
            itens.text = "Itens: " + b

            if (!dbl.haveProdInCarrinho()) activity.finishAfterTransition()
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listCars.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val notificationsEntity = listCars[position]
        bindNotificationItemViewHolder(viewHolder, notificationsEntity)
    }

    private fun bindNotificationItemViewHolder(viewHolder: RecyclerView.ViewHolder, item: CarrinhoModel) {
        val viewHolder = viewHolder as CarrinhoViewHolder

        if (pergMesa) {
            when (item.firstItem) {
                true -> {
                    viewHolder.comandaTitle?.text = titulo + " " + item.numComanda
                    currentComanda = item.numComanda
                    viewHolder.comandaTitle?.visibility = View.VISIBLE
                }
                false -> {
                    viewHolder.comandaTitle?.visibility = View.GONE
                }
                else -> {
                    if (item.numComanda.equals(currentComanda, ignoreCase = true)) {
                        viewHolder.comandaTitle?.visibility = View.GONE
                        item.firstItem = false
                    } else {
                        viewHolder.comandaTitle?.text = titulo + " " + item.numComanda
                        currentComanda = item.numComanda
                        viewHolder.comandaTitle?.visibility = View.VISIBLE
                        item.firstItem = true
                    }
                }
            }
        } else {
            viewHolder.comandaTitle?.visibility = View.GONE
        }

        viewHolder.nome?.text = item.name_prod
        val stringCod = String.format("%14s", item.id_prod).replace(' ', '0')
        viewHolder.cod?.text = stringCod
        viewHolder.qtd?.text = "Quantidade: " + item.qtd_prod

        if (item.acomp_prod.isNullOrEmpty()) {
            viewHolder.aco?.visibility = View.GONE
        } else {
            viewHolder.aco?.visibility = View.VISIBLE
            viewHolder.aco?.text = item.acomp_prod
        }

        if (item.obs_prod.isNullOrEmpty()) {
            viewHolder.obs?.visibility = View.GONE
        } else {
            viewHolder.obs?.visibility = View.VISIBLE
            viewHolder.obs?.text = "Observação: " + item.obs_prod
        }
    }
}

class CarrinhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nome by lazy { itemView?.findViewById<TextView>(R.id.tv_carrinho_nome) }
    val cod by lazy { itemView?.findViewById<TextView>(R.id.tv_carrinho_codigo) }
    val aco by lazy { itemView?.findViewById<TextView>(R.id.tv_carrinho_aco) }
    val del by lazy { itemView?.findViewById<Button>(R.id.bt_carrinho_del) }
    val obs by lazy { itemView?.findViewById<TextView>(R.id.tv_carrinho_obs) }
    val qtd by lazy { itemView?.findViewById<TextView>(R.id.tv_carrinho_qtd) }
    val comandaTitle by lazy { itemView?.findViewById<TextView>(R.id.comandaTitle) }

}