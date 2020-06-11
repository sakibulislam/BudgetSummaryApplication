package bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import oracle.jbo.domain.Number;

import services.AppModuleImpl;

public class Main {
    private RichInputText totalBudget1Binding;
    private RichInputText totalBudget2Binding;
    private RichOutputText ttlBudgetAmt1;
    private RichOutputText ttlBudgetAmt2;

    public Main() {
    }
    
    public ApplicationModule getAppM() {
            DCBindingContainer bindingContainer =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            //BindingContext bindingContext = BindingContext.getCurrent();
            DCDataControl dc =
                bindingContainer.findDataControl("AppModuleDataControl"); // Name of application module in datacontrolBinding.cpx
            AppModuleImpl appM = (AppModuleImpl)dc.getDataProvider();
            return appM;
    }
    AppModuleImpl appM = (AppModuleImpl)this.getAppM();
    
    
    public void showMessage(String message, String severity){
           
           FacesMessage fm = new FacesMessage(message);
           
           if(severity.equals("info")){
               fm.setSeverity(FacesMessage.SEVERITY_INFO);    
           }
           else if (severity.equals("warn")){
               fm.setSeverity(FacesMessage.SEVERITY_WARN);       
           }
           else if (severity.equals("error")){
               fm.setSeverity(FacesMessage.SEVERITY_ERROR);    
           }
           
           FacesContext context = FacesContext.getCurrentInstance();
           context.addMessage(null, fm);
       }

    public void getSummary(ActionEvent actionEvent) {
        // Add event code here...
        try {
            // Add event code here...
            ViewObject ovj  = appM.getSummaryVO1();
            ViewObject ovj1 = appM.getSummary2VO1();
            ViewObject searchVO = appM.getSearchVO1();
            
            String budgetYear1 , budgetYear2, budgetQuarter1 , budgetQuarter2, departmentFrom, departmentTo, orgId1=null, orgId2=null;
            
           budgetYear1 = searchVO.getCurrentRow().getAttribute("BudgetYear1").toString();
           budgetYear2 = searchVO.getCurrentRow().getAttribute("BudgetYear2").toString();
            
           budgetQuarter1 = searchVO.getCurrentRow().getAttribute("BudgetQuarter1").toString();
           budgetQuarter2 = searchVO.getCurrentRow().getAttribute("BudgetQuarter2").toString();
           
            try{ 
               orgId1 = searchVO.getCurrentRow().getAttribute("Org1").toString();
               orgId2 = searchVO.getCurrentRow().getAttribute("Org2").toString();
            }catch(Exception e){
                System.out.println(e);;
            }
            
           departmentTo   = searchVO.getCurrentRow().getAttribute("DepartmentTo").toString();
           departmentFrom = searchVO.getCurrentRow().getAttribute("DepartmentFrom").toString();
              
            int pram=1;
            
            System.out.println(budgetYear1  + ", "  + budgetYear2 + ", " + budgetQuarter1 + 
                               ", " + budgetQuarter2 + ", " + orgId1 + ", " + orgId2 + ", " + departmentFrom + ", " + departmentTo);
            
            if(orgId1 == null && orgId2==null ){
                System.out.println("enter in orgid null condition");
                ovj1.setNamedWhereClauseParam("param",pram);
                ovj1.setNamedWhereClauseParam("B_Q1",budgetQuarter1);
                ovj1.setNamedWhereClauseParam("B_Q2",budgetQuarter2);
                ovj1.setNamedWhereClauseParam("B_year1",budgetYear1);
                ovj1.setNamedWhereClauseParam("B_year2",budgetYear2);
                ovj1.setNamedWhereClauseParam("dept1",departmentFrom);
                ovj1.setNamedWhereClauseParam("dept2",departmentTo);
                System.out.println("Query:" + "\n" + ovj1.getQuery());
                ovj1.executeQuery();
            }else if(orgId1 != null && orgId2!=null  ){
                System.out.println("enter in orgid not null condition");
                ovj.setNamedWhereClauseParam("param",pram);
                ovj.setNamedWhereClauseParam("org1",orgId1);
                ovj.setNamedWhereClauseParam("org2",orgId2);
                ovj.setNamedWhereClauseParam("B_Q1",budgetQuarter1);
                ovj.setNamedWhereClauseParam("B_Q2",budgetQuarter2);
                ovj.setNamedWhereClauseParam("B_year1",budgetYear1);
                ovj.setNamedWhereClauseParam("B_year2",budgetYear2);
                ovj.setNamedWhereClauseParam("dept1",departmentFrom);
                ovj.setNamedWhereClauseParam("dept2",departmentTo);
                System.out.println("Query:" + "\n" + ovj.getQuery());
                ovj.executeQuery();
            }
            
            
            
            
            oracle.jbo.domain.Number firstAmount, ttlFirstAmount, secondAmount, ttlSecondAmount; 
            ttlFirstAmount = new oracle.jbo.domain.Number(0);
            ttlSecondAmount = new oracle.jbo.domain.Number(0);
            
            if(orgId1 == null && orgId2==null ){
                Row[] r1 = ovj1.getAllRowsInRange();
                System.out.println("total rolls number where ordid null ------->" + r1.length);
                for (Row rowEach : r1){
                    firstAmount = (Number)rowEach.getAttribute("BudgetAmount1");
                    ttlFirstAmount= ttlFirstAmount.add(firstAmount);       
                    
                    secondAmount = (Number)rowEach.getAttribute("BudgetAmount2");
                    ttlSecondAmount= ttlSecondAmount.add(secondAmount);            
                }
            }else if(orgId1 != null && orgId2!=null ){
            
                Row[] r = ovj.getAllRowsInRange();
                System.out.println("total rolls number where orgid not null ------->" + r.length);
                for (Row rowEach : r){
                    firstAmount = (Number)rowEach.getAttribute("BudgetAmount1");
                    ttlFirstAmount= ttlFirstAmount.add(firstAmount);       
                    
                    secondAmount = (Number)rowEach.getAttribute("BudgetAmount2");
                    ttlSecondAmount= ttlSecondAmount.add(secondAmount);            
                }
            }
            
            getTtlBudgetAmt1().setValue(ttlFirstAmount);
        
            getTtlBudgetAmt2().setValue(ttlSecondAmount);
            

                 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

   

    public void setTtlBudgetAmt1(RichOutputText ttlBudgetAmt1) {
        this.ttlBudgetAmt1 = ttlBudgetAmt1;
    }

    public RichOutputText getTtlBudgetAmt1() {
        return ttlBudgetAmt1;

    }

    public void setTtlBudgetAmt2(RichOutputText ttlBudgetAmt2) {
        this.ttlBudgetAmt2 = ttlBudgetAmt2;
    }

    public RichOutputText getTtlBudgetAmt2() {
        return ttlBudgetAmt2;
    }
}
