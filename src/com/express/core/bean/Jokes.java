package com.express.core.bean;

import java.util.Random;

public class Jokes {
	/**微信：
[表情]啦啦啦，您的快递我们正飞速处理
笑一个吧[表情]
-你好，怎么称呼？-我姓黄，红绿灯的黄。
肥肉！放开我的腰，有本事冲着我的胸来！
这世间 唯有梦想与好姑娘不可辜负
单子：
[表情]我们应该有梦 有酒 有写不完的诗歌 有坦荡荡的远方
[表情]怕黑开灯 孤独听歌 心塞了去跑步 矫情了就去吃 我们得学会照顾好自己
[表情]有人靠考入清华北大复旦用环境救赎自己,有人发挥失误进入大专也能只身一人闯出一片天地.千万不要认为高考,中考,或一次面试就会决定自己的终身.只要内心有盏灯能够为自己点燃,持续燃烧,每当失去方向的时候,看看内心的光亮,一切都不会惶恐.新学期加油加油！
[表情]我终于相信，每一条走上来的路，都有它不得不那样跋涉的理由。每一条要走下去的路，都有它不得不那样选择的方向。 —席慕容
[表情]Life is a journey.What we should care about is not where it's headed but what we see and how we feel.
	 * */

	private static String[] jokes=new String[]{
			"<(￣ˇ￣)/啦啦啦，您的快递我们正飞速处理",
			"笑一个吧\\(^o^)/~",
			"-你好，怎么称呼？-我姓黄，红绿灯的黄。",
			"肥肉！放开我的腰，有本事冲着我的胸来！",
			"这世间 唯有梦想与好姑娘不可辜负"
	};
	public static String getJoke(){
		
		Random r = new Random();
		
		return "<div class=\"text-primary\">" + jokes[r.nextInt(jokes.length)] + "</div>";
	}
}
