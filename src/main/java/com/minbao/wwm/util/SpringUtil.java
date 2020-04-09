package com.minbao.wwm.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试 和 使用 Spring 里面的 5 个工具类（这样合在一起, 查找  和  使用  比较方便）: 
 * 
 * <br/> 1. org.springframework.util.CollectionUtils;
 * <br/> 2. org.springframework.util.ObjectUtils;
 * <br/> 3. org.springframework.util.ClassUtils;
 * <br/> 4. org.springframework.util.StringUtils;
 * <br/> 5. org.springframework.beans.BeanUtils;
 * 
 * <br/> 另外, split 字符串, 使用了 apache 的 实现.
 * 
 * @author suyulin
 */
public class SpringUtil {
	
	//非汉字 的 字符集 ... 待补充
	public static final String EN_CHAR
		= "0123456789abcdefghijklmnopqrstuvwxyz !@#$%&*()-_=+/,.?;";
	
	
	public static void main(String[] args) {
//		System.out.println(getStringLength("12 345 abcd"));
//		System.out.println(getStringLength("12 345 abcd().,/?-_=+"));
//		System.out.println(getStringLength("12 345 abcd().,/?-_=+ 哈哈我们"));
		System.out.println(subStrByByteLength("我q们都是愤青 Man", 5, "..."));
		BigDecimal total = new BigDecimal(6492782592l);
		BigDecimal use = new BigDecimal(1372309);
		BigDecimal b = new BigDecimal(1024);
		System.out.println(1372309);
		System.out.println(total.divide(b).divide(b).divide(b));
		System.out.println(use.divide(b).divide(b));
	}
	
	
	/**
	 * 得到字符串的长度，一个汉字算2个
	 */
	public static int getStringLength(String str) {
		if(!hasText(str)) {
			return 0;
		}
		
		str = str.trim().toLowerCase();
		int num = 0;
		
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(EN_CHAR.indexOf(c) >= 0) {
				num++;
			} else {
				num += 2;
			}
		}
		
		return num;
	}
	
	
	/**
	 * 如果太长，得到字符串的前 N 个字
	 * 如果太短，给字符串后面补充 空格  ... for: 搜索页面显示商品名称，保证显示 2 行
	 */
	public static String getGoodString(String str, int maxLength) {
		if(hasText(str) && str.length() > maxLength) {
			return str.substring(0, maxLength);
		}
		
		int len = getStringLength(str);
		int goodNum = 38;
		
		if(len < goodNum) {
			int bu = (goodNum - len) * 2;
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			
			for(int i = 0; i < bu; i++) {
				sb.append("&nbsp;");
			}
			
			return sb.toString();
		} else {
			return str;
		}
	}
	
	
	/**
	 * 得到字符串的前 N 个字
	 */
	public static String getPartString(String str, int maxLength) {
		return (hasText(str) && str.length() > maxLength) ? str.substring(0, maxLength): str;
	}
	
	public static String doWithDoubleNumber(BigDecimal num){
		DecimalFormat df = new DecimalFormat("###,##0.00");
		String f2 = df.format(num);
		return f2;
	}
	
	public static String doWithDoubleNumber(String sss){
		return doWithDoubleNumber(new BigDecimal(sss));
	}
	
	/**
	 * 分词全排列, 空格分隔。for: 模糊查询
	 * 
	 * 例如：输入 abcd , 输出:abcd abc ab a bcd bc b cd c d
	 * 
	 * @param maxLengthArray 可变长参数，可以控制  分词全排列  的 最大长度，防止   分词全排列  过大
	 */
	public static String getManyWordSplitBySpaceFull(String str, Integer... maxLengthArray) {
		if(!hasTextAndNotNull(str)) {
			return null;
		}
		
		str = replace(str, " ", ""); //去掉空格
		
		if(!hasTextAndNotNull(str)) {
			return null;
		}
		
		if(SpringUtil.isNotEmpty(maxLengthArray)) {
			int maxLength = maxLengthArray[0];
			str = (str.length() > maxLength) ? str.substring(0, maxLength) : str;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < str.length(); i++) {
			String tempStr = str.substring(i);
			int len = tempStr.length();
			
			for(int j = 0; j < len; j++) {
				sb.append(" ").append(tempStr.substring(0, len - j));
			}
		}
		
		return sb.toString().trim();
	}
	
	/**
	 * 逐字分词，空格分隔。
	 * 
	 * 例如：输入 abcd , 输出:a b c d
	 */
	public static String getManyWordSplitBySpace(String str) {
		if(!hasTextAndNotNull(str)) {
			return null;
		}
		
		str = replace(str, " ", ""); //去掉空格
		
		if(!hasTextAndNotNull(str)) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < str.length(); i++) {
			sb.append(" ").append(str.charAt(i));
		}
		
		return sb.toString().trim();
	}
	
	/**
	 * get Solr SearchText
	 * 
	 * 如果用户输入的查询关键字是用空格隔开的 N 个关键字，则对其进行加工，使其符合 AND 查询
	 * 
	 * 例如：输入 "曼秀雷敦    天然    4g", 输出:(+曼秀雷敦 +天然  +4g)
	 */
	public static String getSolrSearchText(String str) {
		//protected static final String OPERATE_OR = " OR "; //OR 操作符
		if(hasText(str) && str.indexOf(" OR ") >= 0) { //或查询, 不处理了，直接返回
			return str;
		}
		
		if(!hasTextAndNotNull(str) || " ".equals(str)) {
			return null;
		} else if(str.indexOf(" ") < 0) {
			return str;
		}
		
		str = str.trim();
		str = replace(str, "   ", " "); //合并 3 个空格
		str = replace(str, "  ", " "); //合并 2 个空格
		str = replace(str, "  ", " "); //合并 2 个空格
		
		if(!hasTextAndNotNull(str) || " ".equals(str)) {
			return null;
		} else if(str.indexOf(" ") < 0) {
			return str;
		}
		
		String[] array = split(str, " ");
		if(isEmpty(array) || array.length == 1) {
			return str;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		
		int index = 0;
		for(String e: array) {
			if((index++) > 0) {
				sb.append(" ");
			}
			
			sb.append("+").append(e);
		}
		
		sb.append(")");
		
		return sb.toString(); //(+曼秀雷敦 +天然  +4g)
	}
	
	/**
	 * 取得  集合  的前  N 个元素
	 */
	public static <T> List<T> subList(List<T> list, int maxField) {
		return (isNotEmpty(list) && list.size() > maxField) ? list.subList(0, maxField) : list;
	}
	
	/**
	 * 取得  集合  的元素个数
	 */
	public static int sizeOfCollection(Collection<?> collection) {
		return isEmpty(collection) ? 0 : collection.size();
	}
	
	/**
	 * 判断 集合 是不是 空的（null 或者  无元素）
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return CollectionUtils.isEmpty(collection);
	}
	
	/**
	 * 判断 集合 是不是 包含至少一个元素
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	/**
	 * 判断 Map 是不是 空的（null 或者  无元素）
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return CollectionUtils.isEmpty(map);
	}
	
	/**
	 * 判断 Map 是不是 至少包含一个元素
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
	/**
	 * 数组 to:  list集合
	 * 1. 原始类型，会转化为包装类
	 * 2. 返回的  list集合 ，可能  不包含 任何元素，但是 肯定不为 null
	 */
	public static List<?> arrayToList(Object source) {
		return CollectionUtils.arrayToList(source);
	}
	
	/**
	 * 数组 to:  list集合 ... 使用 JDK 自带的 Arrays 工具类
	 */
	public static <T> List<T> arrayToListByJDK(T... array) {
		return isEmpty(array) ? null : Arrays.asList(array);
	}
	
	/**
	 * 把数组合并到集合中
	 * 1. 允许重复元素
	 * 2. 参数: array可以为 null, collection 不能为 null 
	 */
	public static void mergeArrayIntoCollection(Object array, Collection<?> collection) {
//		collection = (null == collection) ? (new ArrayList()) : collection;
		CollectionUtils.mergeArrayIntoCollection(array, collection);
		
//		Iterator iter = collection.iterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
	}
	
	/**
	 * 判断集合中 是否包含 指定的元素
	 * 参数: 2个都可以为 null
	 */
	public static boolean containsInstance(Collection<?> collection, Object element) {
		//return CollectionUtils.containsInstance(collection, element);
		
		if (collection != null) {
			for (Object candidate : collection) {
				if (ObjectUtils.nullSafeEquals(candidate, element)) { //使用 nullSafeEquals
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断集合中 是否 只有一个不重复的元素
	 * 1. 参数: 可以为 null
	 * 2. 例如 collection 是一个 List<String>, 包含 2个元素  "aa", "aa"   ...   那么, 返回 true
	 * 3. 例如 collection 是一个 List<String>, 包含 3个元素  null, null, null   ...   那么, 返回 true
	 * 4. 例如 collection 是一个 List<String>, 包含 2个元素  "aa", "bb"   ...   那么, 返回 false
	 * 5. 例如 collection 是一个 List<String>, 包含 3个元素  "aa", "aa", "bb"   ...   那么, 返回 false
	 */
	public static boolean hasUniqueObject(Collection<?> collection) {
		return CollectionUtils.hasUniqueObject(collection);
	}
	
	/**
	 * Return whether the given throwable is a checked exception:  that is, neither a RuntimeException nor an Error.
	 * 1. 参数: 可以为 null, 返回 true
	 */
	public static boolean isCheckedException(Throwable ex) {
		return ObjectUtils.isCheckedException(ex);
	}
	
	/**
	 * 判断 对象 是不是 数组
	 * 1. 参数: 可以为 null, 返回 false
	 */
	public static boolean isArray(Object obj) {
		//return ObjectUtils.isArray(obj); //Spring 3.0 才加进来
		return (obj != null && obj.getClass().isArray());
	}
	
	/**
	 * 判断 数组 是不是 空的（null 或者  无元素）
	 * 1. 参数: 可以为 null, 返回 true
	 */
	public static boolean isEmpty(Object[] array) {
		return ObjectUtils.isEmpty(array);
	}
	
	/**
	 * 判断 数组 是不是 包含至少一个元素
	 */
	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}
	
	/**
	 * 判断数组中 是否包含 指定的元素
	 * 参数: 2个都可以为 null
	 */
	public static boolean containsElement(Object[] array, Object element) {
		return ObjectUtils.containsElement(array, element);
	}
	
	/**
	 * 把 对象 加入到  数组的最后, 不会去掉重复元素
	 * 参数: 2个都可以为 null, 此时, 返回的数组包含一个元素: null
	 */
	public static Object[] addObjectToArray(Object[] array, Object obj) {
		return ObjectUtils.addObjectToArray(array, obj);
	}
	
	/**
	 * 比较  2个对象  是否相等
	 * 1. 参数: 2个都可以为 null, 此时, 返回 true
	 * 2. 对于数组和 List，如果里面的元素一样，但是元素次序不同，返回  false
	 * 3. 对于 Set 和  Map，如果里面的元素一样，但是元素次序不同，返回  true
	 */
	public static boolean nullSafeEquals(Object o1, Object o2) {
		return ObjectUtils.nullSafeEquals(o1, o2);
	}
	
	/**
	 * nullSafe 的  HashCode
	 * 1. 参数: 可以为 null, 此时, 返回  0
	 */
	public static int nullSafeHashCode(Object obj) {
//		BookVo book = new BookVo.Builder("品三国", 25).author("易中天").page(234).build();
//		BookVo book2 = new BookVo.Builder("品三", 277).author("易中23天").page(30).build();
//		BookVo[] array = new BookVo[2];
//		array[0] = book;
//		array[1] = book2;
//		obj = array;
		
		return ObjectUtils.nullSafeHashCode(obj);
	}

	/**
	 * nullSafe 的  ToString
	 * 1. 参数: 可以为 null, 此时, 返回  null
	 */
	public static String nullSafeToString(Object obj) {
//		int[] tempArray = {11,22,33};
//		obj = tempArray;
		return ObjectUtils.nullSafeToString(obj);
	}
	
	/**
	 * 把 对象 转化为 数组
	 * 1. 参数: 可以为 null, 此时, 返回  空的数组 (注意: 不是null), 不包含任何元素
	 * 2. 参数: 如果不是 null, 也不是一个数组, 会抛出异常:  throw new IllegalArgumentException("Source is not an array: " + source);
	 */
	public static Object[] toObjectArray(Object source) {
		return ObjectUtils.toObjectArray(source);
	}
	
	/**
	 * 属性复制, 版本1
	 * 1. 参数: 2个都    不能    为 null
	 */
	public static void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}
	
	/**
	 * 属性复制, 版本2
	 * 1. 参数: 3个都    不能    为 null
	 * 
	 * @param ignoreProperties 要忽略的   属性   数组, 大小写敏感, 像 "fghjksdsjd" 这样不存在的属性, 不会报错
	 */
	public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
		BeanUtils.copyProperties(source, target, ignoreProperties);
	}
	
	/**
	 * Check if the given type represents a "simple" value type:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale or a Class.
	 * 
	 * 1. 参数: 不能    为 null
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
//		String[] tempArray = {"aa", "bb"};
//		clazz = tempArray.getClass();
//		clazz = int.class;
		
		return BeanUtils.isSimpleValueType(clazz);
	}
	
	/**
	 * Check if the given type represents a "simple" property:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale, a Class, or a corresponding（相应的, 以上类型的）  array.
	 * 
	 * 1. 参数: 不能    为 null
	 */
	public static boolean isSimpleProperty(Class<?> clazz) {
//		AppleVo vo01 = new AppleVo(1, 23, "name01", "red");
//		AppleVo vo02 = new AppleVo(123, 2003, "dfdf", "red");
//		AppleVo[] tempArray = new AppleVo[2];
//		tempArray[0] = vo01;
//		tempArray[1] = vo02;
		
//		String[] tempArray = {"aa", "bb"};
//		clazz = tempArray.getClass();
//		//clazz = int.class;
//		
//		Class<?> cc = clazz.getComponentType(); //得到数组里面的元素类型
//		if(null != cc) {
//			System.out.println("数组里面的元素类型: " + cc.getName());
//		} else {
//			System.out.println("不是数组类型");
//		}
		
		return BeanUtils.isSimpleProperty(clazz);
	}
	
	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
	 * class will be used as fallback.
	 */
	public static ClassLoader getDefaultClassLoader() {
		return ClassUtils.getDefaultClassLoader(); //sun.misc.Launcher$AppClassLoader@19821f
	}
	
	/**
	 * Get the class name without the qualified package name. 考虑到了 CGLIB_CLASS 和 INNER_CLASS
	 * 
	 * 1. 参数: 不能    为 null, 也不能    为 空的字符串 ""
	 */
	public static String getShortName(String className) {
//		className = "com.suyulin.study.BookVo$Builder";
		return ClassUtils.getShortName(className); //BookVo.Builder
	}
	
	/**
	 * Get the class name without the qualified package name. 考虑到了 CGLIB_CLASS 和 INNER_CLASS
	 */
	public static String getShortName(Class<?> clazz) {
//		BookVo.Builder book = new BookVo.Builder("品三国", 25);
//		clazz = book.getClass();
		
//		String[] tempArray = {"aa", "bb", "cc"};
//		clazz = tempArray.getClass();
		
		return ClassUtils.getShortName(clazz); //String[]
	}
	
	/**
	 * Return the qualified name of the given class: usually simply the class name, but component type class name + "[]" for arrays.
	 */
	public static String getQualifiedName(Class<?> clazz) {
//		String[] tempArray = {"aa", "bb", "cc"};
//		clazz = tempArray.getClass();
		
//		BookVo.Builder book = new BookVo.Builder("品三国", 25);
//		clazz = book.getClass(); //QualifiedName: com.suyulin.study.BookVo$Builder
		
		return ClassUtils.getQualifiedName(clazz); //java.lang.String[]
	}
	
	/**
	 * Check if the given class represents a primitive (i.e. boolean, byte,
	 * char, short, int, long, float, or double) or a primitive wrapper
	 * (i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
	 */
	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
//		clazz = double.class;
//		clazz = (new Integer(56)).getClass();
		
		return ClassUtils.isPrimitiveOrWrapper(clazz);
	}
	
	/**
	 * Check if the given class represents a primitive wrapper, i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
//		clazz = double.class; //false
//		clazz = (new Integer(56)).getClass(); //true
		
		return ClassUtils.isPrimitiveWrapper(clazz);
	}
	
	/**
	 * Delete any character in a given String.
	 * @param inString the original String
	 * @param charsToDelete a set of characters to delete.
	 * E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
//		inString = "abcd123efg";
//		charsToDelete = "aaad12efg"; //删除 inString 里面的每一个在 charsToDelete 里面出现过的   char
		
		return StringUtils.deleteAny(inString, charsToDelete); //bc3
	}
	
	/**
	 * Delete all occurrences of the given substring.
	 * @param inString the original String
	 * @param pattern the pattern to delete all occurrences of
	 * @return the resulting String
	 */
	public static String delete(String inString, String pattern) {
//		inString = "_a2b__a2b_a_a2b_bc_a2b__a2b_d_a2b_ef_a2b_";
//		pattern = "_a2b_";
		return StringUtils.delete(inString, pattern); //abcdef
	}
	
	/**
	 * 删除字符串中的所有的  null
	 */
	public static String deleteNull(String inString) {
		return hasText(inString) ? StringUtils.delete(inString, "null") : null;
	}

	/**
	 * Replace all occurences of a substring within a string with
	 * another string.
	 * @param inString String to examine
	 * @param oldPattern String to replace
	 * @param newPattern String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
//		inString = "_a2c__a2c_a_a2c_bcd_a2c_e_a2c_";
//		oldPattern = "_a2c_";
//		newPattern = ".";
		
		return StringUtils.replace(inString, oldPattern, newPattern); //..a.bcd.e.
	}
	
	/**
	 * 把 字符串数组 里的每一个元素 替换成 新的字符串
	 * 
	 * @param oldPatternArray 数组里面的每个字符串，长度都为1
	 */
	public static String replace(String inString, String[] oldPatternArray, String newPattern) {
		if (isEmpty(oldPatternArray)) {
			return inString;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < inString.length(); i++) {
			String charString = String.valueOf(inString.charAt(i));
			sb.append((containsElement(oldPatternArray, charString)) ? newPattern : charString);
		}
		
		return sb.toString();

//		String str = inString;
//
//		if (!isEmpty(oldPatternArray)) {
//			for (String oldPattern : oldPatternArray) {
//				str = replace(inString, oldPattern, newPattern);
//			}
//		}
//
//		return str;
	}
	
	/**
	 * 把字符串分成 “2截”,
	 * 例如: splitTo2("a_b_c_d", "_"), 返回: {"a", "b_c_d"}
	 * 例如: splitTo2("a_b_c_d", "nba"), 返回: null
	 * 
	 * Split a String at the first occurrence of the delimiter.
	 * Does not include the delimiter in the result.
	 * @param toSplit the string to split
	 * @param delimiter to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and
	 * index 1 being after the delimiter (neither element includes the delimiter);
	 * or <code>null</code> if the delimiter wasn't found in the given input String
	 */
	public static String[] splitTo2(String toSplit, String delimiter) {
//		toSplit = "a_b_c_d";
//		delimiter = "_";
		
		String[] array = StringUtils.split(toSplit, delimiter); //如果 toSplit 不包含 delimiter, 返回 null
		
//		System.out.println("array.length = " + array.length); //array.length = 2
//		for(String s: array) {
//			System.out.println(s); //a  和     b_c_d
//		}
		return array;
	}
	
    /**
     * 使用 apache 的 实现, 来 split 字符串.
     * 避免了 JDK String 类 自带的  转义字符问题, 如: "ab.cd.ef".split("\\.")  这样“不舒服”的写法.
     * 采用“舒服”的写法: String[] array = SpringUtil.split("ab.cd.ef", ".");
     * 
     * <p>Splits the provided text into an array, separator string specified.</p>
     *
     * <p>The separator(s) will not be included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separator splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.splitByWholeSeparator(null, *)               = null
     * StringUtils.splitByWholeSeparator("", *)                 = []
     * StringUtils.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separator  String containing the String to be used as a delimiter,
     *  <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String was input
     */
	public static String[] split(String toSplit, String separator) {
		return org.apache.commons.lang.StringUtils.splitByWholeSeparator(toSplit, separator);
	}
	
	/**
	 * return (str != null && str.length() > 0);
	 */
	public static boolean hasLength(CharSequence str) {
		return StringUtils.hasLength(str);
	}
	
	/**
	 * 判断是否有“实际的”（非空格）内容
	 */
	public static boolean hasText(CharSequence str) {
		return StringUtils.hasText(str);
	}
	
	/**
	 * 判断是否有“实际的”（非空格）内容, 并且不为  "null"
	 */
	public static boolean hasTextAndNotNull(String str) {
		return (!"null".equals(str)) && StringUtils.hasText(str);
	}
	
	/**
	 * 如果字符串有“实际的”（非空格）内容, 并且不为  "null"，则加到 StringBuilder 里面
	 */
	public static void addToSbIfHasTextAndNotNull(StringBuilder sb, String str) {
		if(sb != null && hasTextAndNotNull(str)) {
			sb.append(str);
		}
	}
	
	/**
	 * 判断是否有  空格
	 */
	public static boolean containsWhitespace(CharSequence str) {
		return StringUtils.containsWhitespace(str);
	}
	
	/**
	 * Test if the given String starts with the specified prefix, 忽略大小写.
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		return StringUtils.startsWithIgnoreCase(str, prefix);
	}
	
	/**
	 * Test if the given String ends with the specified suffix, 忽略大小写.
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return StringUtils.endsWithIgnoreCase(str, suffix);
	}
	
	/**
	 * Count the occurrences of the substring in string s （计算   一个字符串    出现 在     另 一个字符串中    的次数）
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		return StringUtils.countOccurrencesOf(str, sub);
	}
	
	/**
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 * @param array the array to append to (can be <code>null</code>)
	 * @param str the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		return StringUtils.addStringToArray(array, str);
	}
	
	/**
	 * 合并，不  去掉重复的。合并后，第一个数组的元素在前面
	 */
	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		return StringUtils.concatenateStringArrays(array1, array2);
	}
	
	/**
	 * 合并，去掉重复的。合并后，第一个数组的元素在前面
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		return StringUtils.mergeStringArrays(array1, array2);
	}
	
	/**
	 * Turn given source String array into sorted array.
	 * @param array the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		return StringUtils.sortStringArray(array);
	}
	
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static String[] toStringArray(Collection<String> collection) {
		return StringUtils.toStringArray(collection);
	}
	
	/**
	 * Trim the elements of the given String array,
	 * calling <code>String.trim()</code> on each of them.
	 */
	public static String[] trimArrayElements(String[] array) {
		return StringUtils.trimArrayElements(array);
	}
	
	/**
	 * 去掉   字符串数组   中的重复元素， 并排序
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		return StringUtils.removeDuplicateStrings(array);
	}
	
	////////////////////////////////
	// add
	////////////////////////////////
	

	/**
	 * 说明：这个方法从 Spring 中 BeanPropertyRowMapper 拷贝过来，然后稍作修改。
	 * 在 Spring 的 BeanPropertyRowMapper 类中 ，它是 private 方法
	 * 
	 * 例子：
	 * 输入  name, 返回  name
	 * 输入  cityName, 返回  city_name
	 * 输入  CityName, 返回  city_name
	 * 输入  chinaCarNum, 返回  china_car_num
	 * 
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param name the string containing original name
	 * @return the converted name
	 */
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				char cc = name.charAt(i);
				boolean upperFlag = (cc >= 'A' && cc <= 'Z'); //是否: 大写字母
				//String s = name.substring(i, i + 1);
				String s = String.valueOf(cc);
				
				//if (!Character.isDigit(cc) && s.equals(s.toUpperCase()))
				if (upperFlag) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
    /**
     * 加密, 转成 utf-8 字符串
     */
    public static String encodeToUTF(String key) {
        if (hasText(key)) {
            try {
                key = URLEncoder.encode(key, "utf-8");
            } catch (UnsupportedEncodingException ex) {
            }
        }

        return key;
    }

    /**
     * 解密, 从 utf-8 字符串 还原
     */
    public static String decodeFromUTF(String key) {
        if (hasText(key)) {
            try {
                key = URLDecoder.decode(key, "utf-8");
            } catch (UnsupportedEncodingException ex) {
            }
        }

        return key;
    }
    

    /**
	 * 得到干净的 字符串, 用于  "关键字和标签" 的空格分词
	 */
    public static String getCleanString(String str) {
    	if(!hasText(str)) {
    		return str;
    	}

    	str = str.replace("\n", " "); //去除回车
		str = str.replace("\r", " "); //去除换行
		str = replace(str, "	", " "); //去掉 "奇怪的空格": tab
		
		str = replace(str, ",", " ");
		str = replace(str, "，", " ");
		str = replace(str, ";", " ");
		str = replace(str, "；", " ");
		str = replace(str, "、", " ");
		str = replace(str, "/", " ");
		str = replace(str, ".", " ");
		str = replace(str, "。", " ");
		str = replace(str, "-", " ");
		str = replace(str, "_", " ");
		
		str = replace(str, "      ", " "); //合并 6 个空格
		str = replace(str, "     ", " "); //合并 5 个空格
		str = replace(str, "    ", " "); //合并 4 个空格
		str = replace(str, "   ", " "); //合并 3 个空格
		str = replace(str, "  ", " "); //合并 2 个空格
		str = replace(str, "  ", " "); //合并 2 个空格
		
    	return str;
    }
    
	//test
	public static void main22(String[] args) {
//		String s = "apple23Add45";
//		String str = underscoreName(s);
//		System.out.println(str);
//		str = addUnderscores(s);
//		System.out.println(str);
//		char c = 'Z';
//		boolean bb = (c>='A' && c<='Z');
//		System.out.println(bb);
		
		String str = "            曼秀雷 2s 敦       456  7    8   nba    h   e                       ";
		String aa = getSolrSearchText(str);
		System.out.println("=" + aa + "=");
		
		str = "中华人民共和国";
		aa = getManyWordSplitBySpaceFull(str);
		System.out.println("=" + aa + "=");
	}
	/**
	 * 按字节数截取字符串。
	 * String.valueOf(arr[i]).matches("[\u4e00-\u9fa5]") 只是汉字
	 * String.valueOf(arr[i]).matches("[^x00-xff]") 双字节（包括汉字）
	 * @param str 原字符串
	 * @param byteLength 要截取的字节长度
	 * @param postfix 后缀
	 * @return String
	 */
	public static String subStrByByteLength(String str, int byteLength,String postfix) {
		if (str!=null){
			int sl = str.getBytes().length;
			if (sl > byteLength) {
				StringBuffer sb = new StringBuffer();
				char[] arr = str.toCharArray();
				for (int i = 0, j = 0; i < arr.length && j < byteLength; i++) {
					if (String.valueOf(arr[i]).matches("[^x00-xff]")) {
						j += 2;
					} else {
						j++;
					}
					if (j == byteLength + 1 
							&& String.valueOf(arr[i]).matches("[^x00-xff]")) {
					} else {
						sb.append(arr[i]);
					}
				}
				if(postfix==null){
					postfix="";
				}
				str=sb.toString() + postfix;
			}
		}
		return str;
	}
	
	/**
  	 * 首先，得到大于等于初始容量的“2的幂”，然后再乘以2
  	 * 
  	 * 主要用于：构造 HashMap 时，分配初始容量 ... 注意：HashMap的默认加载因子是0.75
  	 */
	public static int mi(int initialCapacity) {
		// Find a power of 2 >= initialCapacity
		int capacity = 1;
		while (capacity < initialCapacity)
			capacity <<= 1;
		return capacity << 1;
	}
	
	/**
  	 * 首先，得到大于等于"collection容量"的“2的幂”，然后再乘以2
  	 * 
  	 * 主要用于：构造 HashMap 时，分配初始容量 ... 注意：HashMap的默认加载因子是0.75
  	 */
	public static int mi(Collection<?> collection) {
		return mi(sizeOfCollection(collection));
	}
	
	/**
	 * 全是数字,反回true,其它false
	 * @param str
	 * @return
	 */
	public static boolean matchNumber(String str) {
		String regex = "^\\d+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.find();
	}
}
