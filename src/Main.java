import java.security.MessageDigest;


public class Main {
	public static byte buffer[][];
	public static byte[] HashFn(String hashAlgorithm,byte[] plainTxt) {
		byte cyperTxt[];
		try {
			MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
			md.update(plainTxt);
			cyperTxt = md.digest();
		}catch(Exception e){
			cyperTxt=null;//뭘 넣어야하는가?
		}
		return cyperTxt;
	}
	public byte[] strToByte(String plain) {//스트링을 바이트로 변환해서 반
		byte[] cyper = plain.getBytes();
		return cyper;
		
	}
	//바이트 코드 2개 합치기
	static byte[] combine(byte[] left,byte[] right) {
		byte[] comb = new byte[left.length + right.length];
		for(int i=0;i<left.length;i++)
			comb[i]=left[i];
		/* 잘 합쳐지는지 test
		String temp = byteArrayToHex(comb);
		System.out.println(temp);
		temp = byteArrayToHex(right);
		System.out.println(temp);
		*/
		System.arraycopy(right,0,comb,left.length,right.length);
		/*잘 합쳐지는지 test
		temp = byteArrayToHex(comb);
		System.out.println(temp);
		*/
		return comb;
	}
	//바이트 코드를 16진수 스트링으로 바꿔주는 함수
	static String byteArrayToHex(byte[] a) {
	    StringBuilder sb = new StringBuilder();
	    for(final byte b: a)
	        sb.append(String.format("%02x ", b&0xff));
	    return sb.toString();
	}
	//트리 만들
	public static void buildTree(int strLength,String plainTxt[]) {
		int height = (int)(Math.log10(strLength)/Math.log10(2));
		System.out.println(height);
		for(int i=strLength;i<(2*strLength);i++) {
			buffer[i] = HashFn("MD5",plainTxt[i-strLength].getBytes());
			
			
		}
		
		for(int level=height;level>0;level--) {
			if(level == height) {
				for(int width= (int)Math.pow(2, level);width<(int)Math.pow(2, level)+strLength;width+=2) {
					buffer[width/2] = HashFn("MD5",combine(buffer[width],buffer[width+1]));
				}
			}
			for(int width= (int)Math.pow(2, level);width<2*(int)Math.pow(2, level);width+=2) {
				String temp = byteArrayToHex(combine(buffer[width],buffer[width+1]));
				buffer[width/2] = HashFn("MD5",combine(buffer[width],buffer[width+1]));
			}
		}
		
	}
	public static void main(String args[]) {
		String plainTxt[] = {"firtst","second","third","third",/*"hello","papapa"*/};
		buffer = new byte[2*plainTxt.length][];
		buildTree(plainTxt.length,plainTxt);
		
		for(int i=1;i<2*plainTxt.length;i++) {
			String temp = byteArrayToHex(buffer[i]);
			System.out.println(i+" : "+buffer[i]);
			System.out.println(" ==> "+temp);
		}
	}
}
