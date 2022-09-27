package com.sis.clighteningboost.Utills;

import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;
import com.sis.clighteningboost.Models.REST.ClientData;
import com.sis.clighteningboost.Models.REST.FundingNode;
import com.sis.clighteningboost.Models.REST.MerchantData;
import com.sis.clighteningboost.Models.REST.RoutingNode;
import com.sis.clighteningboost.Models.RPC.DecodePayBolt11;
import com.sis.clighteningboost.Models.RPC.InvoiceForPrint;
import com.sis.clighteningboost.Models.RPC.NodeLineInfo;
import com.sis.clighteningboost.Models.RPC.RpcInfo;
import com.sis.clighteningboost.RPC.CreateInvoice;
import com.sis.clighteningboost.RPC.Invoice;
import com.sis.clighteningboost.RPC.Tax;

import java.util.ArrayList;

public class GlobalState {

    private static GlobalState globalState;
    private Tax tax;
    private RpcInfo rpcInfo;
    private String lattitude;
    private String longitude;
    private CreateInvoice createInvoice;
    private Invoice invoice;
    private ArrayList<RoutingNode> nodeArrayList;
    private String clientNodeId;
    private ArrayList<NodeLineInfo> nodeLineInfoArrayList;
    private FundingNode fundingNode;
    private MerchantData mainMerchantData;
    private ClientData   mainClientData;
    private ArrayList<MerchantData> allMerchantDataList;
    private InvoiceForPrint invoiceForPrint;


    public DecodePayBolt11 getCurrentDecodePayBolt11() {
        return currentDecodePayBolt11;
    }

    public void setCurrentDecodePayBolt11(DecodePayBolt11 currentDecodePayBolt11) {
        this.currentDecodePayBolt11 = currentDecodePayBolt11;
    }

    private DecodePayBolt11 currentDecodePayBolt11;


    public MerchantData getMainMerchantData() {
        return mainMerchantData;
    }
    public void setMainMerchantData(MerchantData mainMerchantData) {
        this.mainMerchantData = mainMerchantData;
    }

    public ClientData getMainClientData() {
        return mainClientData;
    }

    public void setMainClientData(ClientData mainClientData) {
        this.mainClientData = mainClientData;
    }

    public ArrayList<MerchantData> getAllMerchantDataList() {
        return allMerchantDataList;
    }

    public void setAllMerchantDataList(ArrayList<MerchantData> allMerchantDataList) {
        this.allMerchantDataList = allMerchantDataList;
    }
    public InvoiceForPrint getInvoiceForPrint() {
        return invoiceForPrint;
    }

    public void setInvoiceForPrint(InvoiceForPrint invoiceForPrint) {
        this.invoiceForPrint = invoiceForPrint;
    }


    public FundingNode getFundingNode() {
        return fundingNode;
    }

    public void setFundingNode(FundingNode fundingNode) {
        this.fundingNode = fundingNode;
    }

    public CurrentAllRate getCurrentAllRate() {
        return currentAllRate;
    }

    public void setCurrentAllRate(CurrentAllRate currentAllRate) {
        this.currentAllRate = currentAllRate;
    }

    private CurrentAllRate currentAllRate;

    public ArrayList<NodeLineInfo> getNodeLineInfoArrayList() {
        return nodeLineInfoArrayList;
    }

    public void setNodeLineInfoArrayList(ArrayList<NodeLineInfo> nodeLineInfoArrayList) {
        this.nodeLineInfoArrayList = nodeLineInfoArrayList;
    }

    public  void  addInNodeLineInfoArrayList(NodeLineInfo nodeLineInfo) {
        if(this.nodeLineInfoArrayList!=null) {
            this.nodeLineInfoArrayList.add(nodeLineInfo);
        }
        else {
            this.nodeLineInfoArrayList=new ArrayList<>();
            this.nodeLineInfoArrayList.add(nodeLineInfo);
        }

    }
    public void removeInNodeLineInfoArrayList(NodeLineInfo nodeLineInfo) {
        if(this.nodeLineInfoArrayList!=null) {
            if(this.nodeLineInfoArrayList.size()>0) {
                this.nodeLineInfoArrayList.remove(nodeLineInfo);
            }

        }

    }

    public String getClientNodeId() {
        return clientNodeId;
    }

    public void setClientNodeId(String clientNodeId) {
        this.clientNodeId = clientNodeId;
    }

    public ArrayList<RoutingNode> getNodeArrayList() {
        return nodeArrayList;
    }

    public void setNodeArrayList(ArrayList<RoutingNode> nodeArrayList) {
        this.nodeArrayList = nodeArrayList;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public CreateInvoice getCreateInvoice() {
        return createInvoice;
    }

    public void setCreateInvoice(CreateInvoice createInvoice) {
        this.createInvoice = createInvoice;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static GlobalState getInstance(){

        if(globalState==null){

            globalState = new GlobalState();

        }

        return  globalState;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public RpcInfo getRpcInfo() {
        return rpcInfo;
    }

    public void setRpcInfo(RpcInfo rpcInfo) {
        this.rpcInfo = rpcInfo;
    }

    public static GlobalState getGlobalState() {
        return globalState;
    }

    public static void setGlobalState(GlobalState globalState) {
        GlobalState.globalState = globalState;
    }

}
