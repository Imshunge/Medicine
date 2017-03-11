package mediatek.android.IoTManager;

/**
 * 摄像头 路径不能 更改
 */
public class ClientInfo
{
	public int ClientID;
	public String VendorName;
	public String IPAddress;
	public String ProductType;
	public String ProductName;
	public void SetID(int ID)
	{
		this.ClientID = ID;
	}
	
	public void SetVendorName(String Vendor)
	{
		this.VendorName = Vendor;
	}
	
	public void SetProductType(String Type)
	{
		this.ProductType = Type;
	}
	
	public void SetProductName(String Name)
	{
		this.ProductName = Name;
	}

	public void SetIPAddr(String IPAddr)
	{
		this.IPAddress = IPAddr;
	}
} 
