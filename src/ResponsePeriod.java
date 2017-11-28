public class ResponsePeriod {
    ResponsePeriod()
    {
        sum=0;
        count=0;
    }
    String serviceComponentName="";
    double sum;
    int count;

    public void SetName(String serviceComponentName){this.serviceComponentName=serviceComponentName;}
    public String GetName (){return this.serviceComponentName;}
    public void SetSum(double sum){this.sum=sum;}
    public double GetSum(){return this.sum;}
    public void SetCounter(int count){this.count=count;}
    public int GetCounter(){return this.count;}
}
