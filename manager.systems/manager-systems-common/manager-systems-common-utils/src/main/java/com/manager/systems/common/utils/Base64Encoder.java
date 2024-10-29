package com.manager.systems.common.utils;

public class Base64Encoder 
{
	private static char[]    map1 = new char[64];
	   static {
		   
	      int i = 0;
	      
	      for (char c = 'A'; c <= 'Z'; c++) map1[i++] = c;
	      for (char c = 'a'; c <= 'z'; c++) map1[i++] = c;
	      for (char c = '0'; c <= '9'; c++) map1[i++] = c;
  
	      map1[i++] = '+'; map1[i++] = '/'; 
	   }

	private static byte[] map2 = new byte[128];
	   static {
	      for (int i=0; i<map2.length; i++) map2[i] = -1;
	      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; 
	   }

	public static String encodeString (String s) {
	   return new String(encode(s.getBytes())); 
	}

	public static char[] encode (byte[] in) {
	   return encode(in,in.length); 
	}

	public static char[] encode (byte[] in, int iLen) {
	   int oDataLen = (iLen*4+2)/3;      
	   int oLen = ((iLen+2)/3)*4;        
	   
	   char[] out = new char[oLen];
	   
	   int ip = 0;
	   int op = 0;
	   
	   while (ip < iLen) 
	   {
	      int i0 = in[ip++] & 0xff;
	      int i1 = ip < iLen ? in[ip++] & 0xff : 0;
	      int i2 = ip < iLen ? in[ip++] & 0xff : 0;
	      int o0 = (i0 & 0xFC) >> 2;
	      int o1 = ((i0 & 0x03) << 4) | ((i1 & 0xF0) >> 4);
	      int o2 = ((i1 & 0x0F) << 2) | ((i2 & 0xC0) >> 6);
	      int o3 = i2 & 0x3F;
	      out[op++] = map1[o0];
	      out[op++] = map1[o1];
	      out[op] = op < oDataLen ? map1[o2] : '='; op++;
	      out[op] = op < oDataLen ? map1[o3] : '='; op++; 
	   }
	   
	   return out; 
	}

	/*
	public static char[] encodeC (byte[] in) {
	   return encodeC(in,in.length); 
	}
	
	
	public static char[] encodeC (byte[] bytes, int iLen) {
		int oLen = ((iLen+2)/3)*4;   
		int outCount= 0;
		char[] out = new char[oLen];
		
		int ixtext  = 0;
	    int lentext = bytes.length;
		int ctremaining      = 0;
		int inbuf[] = new int[3];
		int outbuf[] = new int[4];	    
		short i           = 0;
		short charsonline = 0, ctcopy = 0;
		int ix  = 0;
		
		
		while( true ) 
	    {
			ctremaining = lentext - ixtext;
		
	        if( ctremaining <= 0 )
	        {
	            break;
	        }
			
			for(i = 0; i < 3; i++) 
	        {
				ix = ixtext + i;
				
	            if(ix < lentext) 
	            {
	                inbuf[i] = bytes[ix];
	            }
				
	            else 
	            {
	                inbuf [i] = 0;
	            }
			}
			
			outbuf [0] = (inbuf [0] & 0xFC) >> 2;
			outbuf [1] = ((inbuf [0] & 0x03) << 4) | ((inbuf [1] & 0xF0) >> 4);
			outbuf [2] = ((inbuf [1] & 0x0F) << 2) | ((inbuf [2] & 0xC0) >> 6);
			outbuf [3] = inbuf [2] & 0x3F;
			ctcopy     = 4;
			
			switch (ctremaining) 
	        {
				case 1: 
	                
					ctcopy = 2; 
					break;
	                
				case 2: 
	                
					ctcopy = 3; 
					break;
			}
			
			for (i = 0; i < ctcopy; i++)
	        {
				out[outCount++] = map1[outbuf[i]];
			}
	        
			for( i = ctcopy; i < 4; i++ )
	        {
				out[outCount++] = '=';
			}
	        
			ixtext      += 3;
			charsonline += 4;
			
			/*if(lineLength > 0) 
	        {
				if (charsonline >= lineLength) 
	            {
					charsonline = 0;
					
					//[result appendString:@"\n"];
				}
			}
		}
		
		return out;
	}*/
	
	public static String decodeString (String s) {
	   return new String(decode(s)); 
	}

	public static byte[] decode (String s) {
	   return decode(s.toCharArray()); 
	}

	public static byte[] decode (char[] in) {
	   int iLen = in.length;
	   
	   if (iLen%4 != 0) 
	   {
		   throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");
	   }
	   
	   while (iLen > 0 && in[iLen-1] == '=') 
	   {
		   iLen--;
	   }
	   
	   int oLen = (iLen*3) / 4;
	   
	   byte[] out = new byte[oLen];
	   
	   int ip = 0;
	   int op = 0;
	   
	   while (ip < iLen) {
	      
		   int i0 = in[ip++];
		   int i1 = in[ip++];
		   int i2 = ip < iLen ? in[ip++] : 'A';
		   int i3 = ip < iLen ? in[ip++] : 'A';
	      
		   if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
		   {
			   throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
		   }
		   
		   int b0 = map2[i0];
		   int b1 = map2[i1];
		   int b2 = map2[i2];
		   int b3 = map2[i3];
		   
		   if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
		   {
			   throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
		   }
	         
		   int o0 = (b0 << 2) | (b1 >>> 4);
		   int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
		   int o2 = ((b2 &   3) << 6) |  b3;
	      
		   out[op++] = (byte)o0;
	      
		   if (op<oLen) out[op++] = (byte)o1; 
		   if (op<oLen) out[op++] = (byte)o2; 
	   	}
	   
	   return out; 
	}

	private Base64Encoder() 
	{ 
		//		
	}
}