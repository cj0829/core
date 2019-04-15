package org.csr.core.util;

public class NationUtil {
	public static Byte getNationString(String nation) {
		if (nation == null || nation.equals("")) {
			return 57;
		}
		switch (nation) {
		case "汉":

			return 1;
		case "蒙古":

			return 2;

		case "回":

			return 3;
		case "藏":

			return 4;

		case "维吾尔":

			return 5;
		case "苗":

			return 6;

		case "彝":

			return 7;
		case "壮":

			return 8;
		case "布依":

			return 9;
		case "朝鲜":

			return 10;
		case "满":

			return 11;
		case "侗":

			return 12;

		case "瑶":

			return 13;
		case "白":

			return 14;

		case "土家":

			return 15;
		case "哈尼":

			return 16;

		case "哈萨克":

			return 17;
		case "傣":

			return 18;

		case "黎":

			return 19;
		case "傈僳":

			return 20;
		case "佤":

			return 21;
		case "畲":

			return 22;
		case "高山":

			return 23;
		case "拉祜":

			return 24;
		case "水":

			return 25;

		case "东乡":

			return 26;
		case "纳西":

			return 27;
		case "景颇":

			return 28;
			
		case "柯尔克孜":
			return 29;
		case "土":
			return 30;
		case "达斡尔":
			return 31;
		case "仫佬":
			return 32;
		case "羌":
			return 33;
		case "布朗":
			return 34;
		case "撒拉":
			return 35;
		case "毛南":
			return 36;
		case "仡佬":
			return 37;
		case "锡伯":
			return 38;
		case "阿昌":
			return 39;
		case "普米":
			return 40;
		case "塔吉克":
			return 41;
		case "怒":
			return 42;
		case "乌孜别克":
			return 43;
		case "俄罗斯":
			return 44;
		case "鄂温克":
			return 45;
		case "崩龙":
			return 46;
		case "保安":
			return 47;
		case "裕固":
			return 48;
		case "京":
			return 49;
		case "塔塔尔":
			return 50;
		case "独龙":
			return 51;
		case "鄂伦春":
			return 52;
		case "赫哲":
			return 53;
		case "门巴":
			return 54;
		case "珞巴":
			return 55;
		case "基诺":
			return 56;
		case "其他":
			return 57;
		case "外国血统中国籍人士":
			return 58;
		default:
			return 57;
		}

	}
	public static String getNation(Byte nation) {
		if(ObjUtil.isEmpty(nation)){
			return "外国血统中国籍人士";
		}
		switch (nation) {
		case 1:
			return "汉";
		case 2:
			return "蒙古";
		case 3:
			return "回";
		case 4:
			return "藏";
		case 5:
			return "维吾尔";
		case 6:
			return "苗";
		case 7:
			return "彝";
		case 8:
			return "壮";
		case 9:
			return "布依";
		case 10:
			return "朝鲜";
		case 11:
			return "满";
		case 12:
			return "侗";
		case 13:
			return "瑶";
		case 14:
			return "白";
		case 15:
			return "土家";
		case 16:
			return "哈尼";
		case 17:

			return "哈萨克";
		case 18:

			return "傣";

		case 19:

			return "黎";
		case 20:

			return "傈僳";
		case 21:

			return "佤";
		case 22:

			return "畲";
		case 23:

			return "高山";
		case 24:

			return "拉祜";
		case 25:

			return "水";

		case 26:

			return "东乡";
		case 27:

			return "纳西";
		case 28:
			return "景颇";
		case 29:
			return "柯尔克孜";
		case 30:
			return "土";
		case 31:
			return "达斡尔";
		case 32:
			return "仫佬";
		case 33:
			return "羌";
		case 34:
			return "布朗";
		case 35:
			return "撒拉";
		case 36:
			return "毛难";
		case 37:
			return "仡佬";
		case 38:
			return "锡伯";
		case 39:
			return "阿昌";
		case 40:
			return "普米";
		case 41:
			return "塔吉克";
		case 42:
			return "怒";
		case 43:
			return "乌孜别";
		case 44:
			return "俄罗斯";
		case 45:
			return "鄂温克";
		case 46:
			return "崩龙";
		case 47:
			return "保安";
		case 48:
			return "裕固";
		case 49:
			return "京";
		case 50:
			return "塔塔尔";
		case 51:
			return "独龙";
		case 52:
			return "鄂伦春";
		case 53:
			return "赫哲";
		case 54:
			return "门巴";
		case 55:
			return "珞巴";
		case 56:
			return "基诺";
		case 57:
			return "其他";
		case 58:
			return "外国血统中国籍人士";
		default:
			return "其他";
		}

}
	public static void main(String[] args) {
	}
}
