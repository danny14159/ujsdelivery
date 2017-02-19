/**
 * 命名空间配置定义
 */
var one = one || {
        version: "1.0",
        coreFileName: "one.js",
        namespace: "one"
    };


/**
 * 常量定义 dictionary
 */
one.dict = {
    success: 'success',
    error: 'error',
    fail: 'fail',
    dataPrefix: "one-data-"//ui组件自定义属性前缀
};

/**
 * 组件缓存,以"模块-组件名-id"格式作为key存储.
 * 例如:ui-button-id
 */
one.assemblyCache = {};


/**
 *
 * util
 *
 */
(function ($) {

    /**
     * 模块初始化
     */
    var exports = {},
        _this = this;

    /**
     * 方法提供到util命名空间下
     */
    this.util = exports;

    /**
     * 判断是否为数组类型
     */
    exports.isArray = function (o) {
        return o !== null && typeof o === "object" && 'splice' in o && 'join' in o;
    };

    /**
     * 判断是否为object类型
     */
    exports.isObject = function (o) {
        return typeof o === "object";
    };

    /**
     * 判断是否为string类型
     */
    exports.isString = function (o) {
        return typeof o === "string";
    };

    /**
     * 判断是否为function类型
     */
    exports.isFunction = function (o) {
        return typeof o === "function";
    };

    /**
     * 判断是否为boolean类型
     */
    exports.isBoolean = function (o) {
        return typeof o === "boolean";
    };

    /**
     * 判断是否为number类型
     */
    exports.isNumber = function (o) {
        return typeof o === "number";
    };

    /**
     * 判断是否为undefined
     */
    exports.isUndefined = function (o) {
        return typeof o === "undefined";
    };

    /**
     * 判断对象是否为空字符串，去除前后空格
     * @param o
     * @returns {boolean|*}
     */
    exports.isBlank = function (o) {
        o = o || "";
        return o === "" || o.trim() === "";
    };


    /**
     * 设置dom对象css属性值
     * @param element dom元素(zepto对象)
     * @param options 值列表(json)
     */
    exports.setElementCss = function (element, options) {
        if (element && this.isObject(options)) {
            for (var att in options) {
                element.css(att, options[att]);
            }
        }
    };
    /**
     * 修改页面当中的style标签且title值为changeThemes的标签内容
     * 用于js动态修改页面内嵌样式的值
     * @param data
     * 示例：
     *  <style title="changeThemes">
     *      //可供修改组件的样式,占位使用，通过此选择器可以使用js复写css文件中已经定义的样式
     *      .one_switch__input:checked + .one_switch__toggle{
     *      }
     *   </style>
     * one.util.changeThemes({
                    ".one_switch__input:checked + .one_switch__toggle":{"background":“red”}
                });
     */
    exports.changeThemes = function (data) {
        if (!one.util.isUndefined(data)) {
            $.each(document.styleSheets, function () {
                var title = $(this).get(0).title;
                if (title === "changeThemes") {
                    $.each($(this).get(0).cssRules, function () {
                        var selectorText = $(this).get(0).selectorText;
                        var styles = data[selectorText];
                        if (styles !== null) {
                            for (var key in styles) {
                                $(this).get(0).style[key] = styles[key];
                            }
                        }
                    });
                }
            });
        }
    };

    /**
     * 设置dom对象style属性值
     * @param element dom元素(zepto对象)
     * @param value 值
     */
    exports.setElementStyle = function (element, value) {
        if (element && typeof value === "string") {
            element.attr("style", value);
        }
    };

    /**
     * 设置dom对象自定义属性以及值
     * @param element dom元素(zepto对象)
     * @param attributes 自定义属性列表(Array or String)
     * @param values 自定义属性列表值(json-key需要和attributes对应 or String)
     */
    exports.setElementData = function (element, attributes, values) {
        if (this.isObject(values) && this.isArray(attributes)) {
            for (var att in attributes) {
                element.attr(_this.dict.dataPrefix + attributes[att], values[attributes[att]]);
            }
        } else if (this.isString(values) && this.isString(attributes)) {
            element.attr(_this.dict.dataPrefix + attributes, values);
        }
    };

    /**
     * 获取设置在dom属性上自定义属性的值
     * @param element dom元素(zepto对象)
     * @param attribute 自定义属性名称
     */
    exports.getElementData = function (element, attribute) {
        if (this.isString(attribute)) {
            return element.attr(_this.dict.dataPrefix + attribute);
        }
        return null;
    };

    /**
     * 处理用户调用组件时传入的参数
     * @param param 用户调用组件时传入的参数
     * @param attributes 自定义属性列表(Array)
     */
    exports.getParams = function (param, attributes) {
        var result = null;

        if (this.isString(param)) {//如果是string，作为按钮id
            result = this.getElementParams(param, attributes);
        } else if (this.isObject(param)) {//如果是json格式
            var t = this.getElementParams(param.id, attributes);
            if (t) {
                //用新传入的参数值覆盖之前存储在dom元素上的json值
                result = $.extend(t, param);
            }
        }

        //如果result不为null则表示该组件已经渲染过(用id作为唯一标示),不需要再次渲染
        if (result) {
            result.isRender = false;
        }
        return result || param;
    };

    /**
     * 在指定元素ID中得到传入的自定义属性列表和值，生成json返回
     * @param elementId 元素ID
     * @param attributes 自定义属性列表(Array)
     */
    exports.getElementParams = function (elementId, attributes) {
        var result = null,
            element = $("#" + elementId);

        if (element.size() > 0) {
            element = element.eq(0);
            result = {id: elementId};
            for (var att in attributes) {
                var v = element.attr(_this.dict.dataPrefix + attributes[att]);
                if (v) {
                    result[attributes[att]] = v;
                }
            }
        }
        return result;
    };

    /**
     * console.log
     * @param msg 需要打印的消息
     */
    exports.log = function (msg) {
        if (window.console) {
            console.log(msg);
        }
    };

    /**
     * 得到核心one.js文件引用路径
     */
    exports.coreOneUrl = function () {
        var srcUrl = "",
            fileReg = new RegExp(_this.coreFileName, "i");

        $("script").each(function () {
            if (fileReg.test($(this).attr("src"))) {
                srcUrl = $(this).attr("src").replace(_this.coreFileName, "");
                return false;
            }
        });

        return srcUrl;
    }();

    /**
     * 检查JS文件或者CSS文件是否存在
     */
    exports.existJsCssFile = function (fileName) {
        var type = "";

        if (/\.js$/.test(fileName)) {
            type = "js";
        }
        else if (/\.css$/.test(fileName)) {
            type = "css";
        }

        var reg = new RegExp(fileName, "i"),
            result = false,
            filter = type === "js" ? "script[src]" : "";
        filter = type === "css" ? "link[href]" : filter;

        if (type === "js" || type === "css") {
            var attr = filter.match(/\[.+\]/)[0].replace("[", "").replace("]", "");
            $(filter).each(function () {
                if (reg.test($(this).attr(attr))) {
                    result = true;
                    return false;
                }
            });
        }

        return result;
    };

    /**
     * 动态加载js文件以及css文件
     * @param  {filename} 文件名 String or Array
     * @param  {charset}  文件编码
     * @param  {callback(code)} 文件加载完成回调函数 code[success,error]
     */
    exports.loadJsCssFile = function (params) {
        var dp = {
                filename: null,//array in filename[{filename:'',media:'',charset:'',ftype:''}]
                charset: null,
                media: null,
                ftype: null,
                callback: function (code) {
                }
            },
            _index = -1,
            _util = this;

        $.extend(dp, params);
        function loadFile(filename, charset, media, callback, ftype) {
            var fileref, src = filename, filetype, checkFile = true;

            if (_util.isObject(filename)) {
                charset = filename.charset || charset;
                media = filename.media || media;
                src = filename.filename;
                ftype = filename.ftype;
                checkFile = _util.isBoolean(filename.checkFile) ? filename.checkFile : true;
            }

            filetype = src;

            if (!filetype) {
                _util.isFunction(callback) && callback(_this.dict.success);
                return;
            } else if (checkFile && _util.existJsCssFile(src)) {
                _util.isFunction(callback) && callback(_this.dict.success);
                return;
            }

            filetype = filetype.substring(filetype.lastIndexOf(".") + 1).toLowerCase();
            filetype = ftype || filetype;

            //createElement
            if (/^js/i.test(filetype)) {
                fileref = document.createElement('script');
                fileref.setAttribute("type", "text/javascript");
                fileref.setAttribute("src", src);
            } else if (/^css/i.test(filetype)) {
                fileref = document.createElement('link');
                fileref.setAttribute("rel", "stylesheet");
                fileref.setAttribute("type", "text/css");
                fileref.setAttribute("href", src);
            } else {//如果非此两种文件
                _util.isFunction(callback) && callback(_this.dict.error);
            }

            //event and callback bind
            if (fileref !== undefined) {
                charset && fileref.setAttribute("charset", charset);
                media && fileref.setAttribute("media", media);
                if (filetype === "css") {//css 的onload不兼容所有浏览器
                    _util.isFunction(callback) && callback(_this.dict.success);
                } else {
                    fileref.onload = fileref.onreadystatechange = function () {
                        if (!this.readyState ||
                            this.readyState === 'loaded' ||
                            this.readyState === 'complete') {
                            _util.isFunction(callback) && callback(_this.dict.success);
                        }
                    }
                }
                fileref.onerror = function () {
                    _util.isFunction(callback) && callback(_this.dict.error);
                }
                document.getElementsByTagName("head")[0].appendChild(fileref);
            }
        }

        if (this.isArray(dp.filename)) {
            (function () {
                _index++;

                if (_index >= dp.filename.length) {
                    dp.callback(_this.dict.success);
                    return;
                }

                loadFile(dp.filename[_index], dp.charset, dp.media, arguments.callee, dp.ftype);
            })();
        } else {
            loadFile(dp.filename, dp.charset, dp.media, dp.callback, dp.ftype);
        }
    };

    /**
     * 得到地址栏参数
     * @param names 参数名称
     * @param urls 从指定的urls获取参数
     * @returns string
     */
    exports.getQueryString = function (names, urls) {
        urls = urls || window.location.href;
        urls && urls.indexOf("?") > -1 ? urls = urls.substring(urls.indexOf("?") + 1) : "";
        var reg = new RegExp("(^|&)" + names + "=([^&]*)(&|$)", "i");
        var r = urls ? urls.match(reg) : window.location.search.substr(1).match(reg);
        if (r !== null && r[2] !== "")return unescape(r[2]);
        return null;
    };

    /**
     * setLocalStorage
     * @param key key
     * @param value value
     * @param isJson 是否json格式
     */
    exports.setLocalStorage = function (key, value, isJson) {
        if (window.localStorage) {
            if (isJson) {
                value = JSON.stringify(value);
            }
            localStorage[key] = value;
        }
    };

    /**
     * getLocalStorage
     * @param key key
     * @param isJson 是否json格式
     */
    exports.getLocalStorage = function (key, isJson) {
        if (window.localStorage) {
            var value = localStorage[key] || "";
            if (isJson && value) {
                value = JSON.parse(value);
            }
            return value;
        }
    };

    /**
     * removelocalStorage
     * @param key key
     */
    exports.removelocalStorage = function (key) {
        if (window.localStorage) {
            localStorage.removeItem(key);
        }
    };

    /**
     * setSessionStorage
     * @param key key
     * @param value value
     * @param isJson 是否json格式
     */
    exports.setSessionStorage = function (key, value, isJson) {
        if (window.sessionStorage) {
            if (isJson) {
                value = JSON.stringify(value);
            }
            sessionStorage[key] = value;
        }
    };

    /**
     * getSessionStorage
     * @param key key
     * @param isJson 是否json格式
     */
    exports.getSessionStorage = function (key, isJson) {
        if (window.sessionStorage) {
            var value = sessionStorage[key] || "";
            if (isJson && value) {
                value = JSON.parse(value);
            }
            return value;
        }
    };

    /**
     * removeSessionStorage
     * @param key key
     */
    exports.removeSessionStorage = function (key) {
        if (window.sessionStorage) {
            sessionStorage.removeItem(key);
        }
    };

    /**
     * getCookie
     * @param key key
     */
    exports.getCookie = function (key) {
        var arr, reg = new RegExp("(^| )" + key + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            return unescape(arr[2]);
        } else {
            return null;
        }
    };

    /**
     * removeCookie
     * @param param {domain,key,path}
     */
    exports.removeCookie = function (param) {
        var dp = {
            domain: "",
            key: "",
            path: "/"
        };

        if (this.isObject(param)) {
            $.extend(dp, param);
        } else if (this.isString(p)) {
            dp.key = param;
        }

        if (!dp.domain) {
            dp.domain = function () {
                return document.domain;
            }();
        }

        var exp = new Date("2000", "1", "1");
        var cval = this.getCookie(dp.key);

        if (cval !== null) {
            document.cookie = dp.key + "=" + cval + ";domain=" + dp.domain + ";path=" + dp.path + ";expires=" + exp.toGMTString();
        }
    };

    /**
     * getPlatform
     */
    exports.getPlatform = function () {
        var platforms = {
            amazon_fireos: /cordova-amazon-fireos/,
            android: /Android/,
            ios: /(iPad)|(iPhone)|(iPod)/,
            blackberry10: /(BB10)/,
            blackberry: /(PlayBook)|(BlackBerry)/,
            windows8: /MSAppHost/,
            windowsphone: /Windows Phone/
        };
        for (var key in platforms) {
            if (platforms[key].exec(navigator.userAgent)) {
                return key;
            }
        }
        return "";
    };

    /**
     * 得到组件缓存对象
     */
    exports.getAssemblyCache = function (moduleId) {
        return _this.assemblyCache[moduleId];
    };

    /**
     * 添加组件缓存对象
     */
    exports.addAssemblyCache = function (moduleId, object) {
        return _this.assemblyCache[moduleId] = object;
    };

    /**
     * 转换成bool值
     */
    exports.convertBool = function (v) {
        var trues = {
            "1": true,
            "true": true
        };
        return trues[v.toString()] ? true : false;
    };

    /**
     * 转换成int值
     */
    exports.convertInt = function (v) {
        return parseInt(v);
    }

}).call(one, Zepto);




/**
 * backtop
 * required util.js
 */
(function($){
	
	this.ui = this.ui || {}; 
	var util = this.util, 
		exports = this.ui, 
		_version="1.0.0",
		_id=0,//id
		_catchPrefix="ui-backTop-",//组件缓存对象前缀
		_idPrefix=this.namespace + "-backTop-";//自定义id前缀

		
		
	//主函数
	function backTop(param)
	{
	
		var main={
				
				// 组件版本号
				version:_version,
				
				//默认参数
				options:{
					isAnimate : true,
					style : 'right:5%; bottom:5%;',
					id : 'body'
				},
				
				//储存初始化参数
				param:param,
				
				// 缓存组件对象
				backTop:null,
				
				//运动函数
				toTop:function(obj,time,target){
					
				if(time==0||time==''){
					document.body.scrollTop=document.documentElement.scrollTop=0;
					return false;
				}
				
				var count=parseInt(time/30); 
				var scrollTop=parseInt(document.documentElement.scrollTop)||parseInt(document.body.scrollTop);//起点
				var distance=target-scrollTop; //距离
                    console.log('distance='+distance);
				var n=0; //计数器
				
				clearInterval(obj.timer1);
				obj.timer1=setInterval(function(){
					n++;
					if(n==count){
						clearInterval(obj.timer1);
					}
					
					//加速运动
					var a=n/count;

					var cur=scrollTop+distance*(a*a*a);

					obj.documentElement.scrollTop=cur;
					obj.body.scrollTop=cur;
				},30);
					
			},   //运动函数结束
				
				// 模板
				_template:'<div class="one-backtop"></div>',
				
				//初始化参数
				init:function(){
					var _me=this;
					if(util.isObject(this.param)){
						
						//合并参数
						$.extend(this.options,this.param);
						
						//如果没有id,刚赋值默认id
						if(!this.param.id){
							this.param.id=_idPrefix+(++_id);
						}
						
						//创建zepto对象
						this.backTop=$(this._template);

						util.setElementStyle(this.backTop,this.param.style);
												
						(this.backTop).appendTo('body');
						
					}else if(util.isString(this.param)){
						
						this.backTop=$(this.param);
						
					}
					
					//滚轮事件
					$(window).on('scroll',function(){
						
						if ($(window).scrollTop()>50){
							main.backTop.show();
						}
						else{
							main.backTop.hide();
						}
					});
					
					//点击事件
					this.backTop.click(function(){
						
						console.log(_me.options.isAnimate);

						//main.isAnimate(main.options.isAnimate)
						if(main.param.isAnimate==false){
							//直接回到顶部
							main.direct();
							
						}else{
							//600毫秒回到顶部
							main.animate();
						}
					});
					
					
				},//init函数结束
				
				animate:function(){
					main.toTop(document,500,0);
					return false;
				},
				direct:function(){
					main.toTop(document,0,0);
					return false;
				}
				

			};//main函数结束
			
			main.init();
		
	} //主函数结束

	exports.backTop=function(param){
        
		if(util.isString(param)){
			//string 选择器 #id .class
			return new backTop(param);
			
		}else if(util.isObject(param)){
			
			var toTop;
			
			toTop=new backTop(param);
			            
			return toTop;
		}
		
		return null;
       
    };
	
}).call(one,Zepto);




		

/**
 * button
 * @author 易丙军
 * @version 1.0.0
 * required Zepto.js cpic.js util.js
 */
;
(function ($) {
		
	this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
	var util = this.util, //用变量存储util.js中的方法，方便调用
		exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
		me = this, //存储当前this对象(目前指向cpic)，为this对象并不为cpic的方法中提供使用
		_version="1.0.0";//组件版本号

	/*按钮结构模板*/
	var _template = '<button class="one_button"></button>',

	/*默认参数*/
	defaultOptions = function () {
		return {
			icon : "",
			style : "",
			text : "Button",
			width : "auto",
			height : "",
			id : "",
			container : "body", //渲染button的容器
			isRender : true
		};
	},

	/*自定义属性*/
	attribute = [
		"icon",
		"style",
		"text",
		"width",
		"height",
		"id"
	],

	/*buttonId*/
	_bId = 0;

	/**
	 * 渲染button
	 **/
	function renderButton(options) {
		
		//button组件版本号
		this.version=_version;
		
		if (options.isRender) { //如果需要渲染该按钮
			this.button = $(_template);
		} else {
			//不渲染该按钮直接获取指定ID的dom元素
			this.button = $("#" + options.id);
		}

		//如果未传入id,使用自定义规则生成的ID
		if (!options.id && options.isRender) {
			options.id = me.namespace + "-button-" + (++_bId);
		}

		//设置自定义属性，把自定义属性储存在dom节点上，为下次重新获取该按钮提供之前传入的参数
		//util.setElementData(this.button, attribute, options);//暂时不保存参数到dom自定义属性中

		//先设置按钮style属性值，再设置宽和高，避免样式覆盖
		util.setElementStyle(this.button, options.style);

		//设置CSS样式
		util.setElementCss(this.button, {
			width : options.width,
			height : options.height
		});

		//如果需要生成html,则把html追加到指定容器中
		if (options.isRender) {
			this.button.attr("id", options.id); //设置ID
			$(options.container).append(this.button); //追加html
		}
	}

	//原型方法
	renderButton.prototype = {
		setText : function (text) { //设置按钮文本
			util.setElementData(this.button, "text", text);
			this.button.text(text);
		},
		getText : function () { //获取按钮文本
			return this.button.text();
		},
		click : function (callback) { //绑定点击事件
			this.button.off("tap click").on("tap click", function () {
				util.isFunction(callback) && callback.call(this, arguments);
			});
		},
		ready : function (callback) { //组件初始化完成事件
			util.isFunction(callback) && callback.call(this);
		},
		longtap : function (callback) { //绑定长按事件
			this.button.off("longTap").on("longTap", function () {
				util.isFunction(callback) && callback.call(this, arguments);
			});
		}
	};

	/**
	 * 事件绑定
	 **/
	function bindEvent(but, options) {
		if (util.isFunction(options.click)) {
			but.click(options.click);
		}
		if (util.isFunction(options.longtap)) {
			but.longtap(options.longtap);
		}

		//最后直接调用组件初始化完成
		if (util.isFunction(options.ready)) {
			but.ready(options.ready);
		}
	}

	//扩展BUTTON组件
	exports.button = function (param) {
		//处理用户调用组件时传入的参数
		param = util.getParams(param, attribute);
		var but;
		//如果传入值中有ID，则尝试获取缓存对象
		if (param.id) {
			but = util.getAssemblyCache("ui-button-" + param.id);
			//如果有缓存对象则返回缓存对象
			if (but) {
				return but;
			}
		}

		//重新new一个默认参数，如果不使用new获取的会是上一次的值
		var options = new defaultOptions();
		//使用传入的参数覆盖默认参数
		$.extend(options, param);

		//处理组件
		but = new renderButton(options);
		//默认赋值按钮文本
		but.setText(options.text);

		//调用事件绑定方法
		bindEvent(but, options);

		//把组件对象添加到缓存对象中
		util.addAssemblyCache("ui-button-" + options.id, but);

		//返回当前组件对象
		return but;
	};

}).call(one, Zepto);

/**
 * checkBox
 * @author 程伟凡
 * @version 1.0.0
 * required Zepto.js cpic.js
 */
;
(function ($) {

    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version = "1.0.0",//组件版本号
        _id = 0,//id
        _name = 0,//name
        _catchPrefix = "ui-checkBox-",//组件缓存对象前缀
        _idPrefix = this.namespace + "-checkBox-";//自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function checkBox(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version: _version,

            /**
             * 默认参数
             */
            options: {
                checkBoxJson: [{text: "text", value: "value", checked: true, enable: true}],
                container: ".effectShow",//渲染容器
                isVertical: false, //是否垂直
                initFinish: function () {
                },//组件初始化完成
                changeSelect: function () {
                },//复选框选中或取消
                changeStatus: function () {
                }//启用或禁用复选框

            },

            /**
             * 储存初始化参数
             */
            param: param,

            /**
             * 缓存组件对象
             */
            checkBox: null,

            /**
             * 回调函数集合
             */
            callBacks: {},


            /*按钮结构模板*/
            _template: '<label class="one_checkbox">'
            + '<input type="checkbox">'
            + '<div class="one_checkbox__checkmark"></div><span></span></label>',

            /**
             * 初始化组件函数
             */
            init: function () {
                var _me = this;

                if (util.isObject(this.param)) {

                    $.extend(this.options, this.param);

                    for (var i = 0; i < _me.options.checkBoxJson.length; i++) {
                        if (!_me.options.checkBoxJson[i].id) {
                            _me.options.checkBoxJson[i].id = _idPrefix + (++_id);
                        }

                        //查找组件
                        _me.checkBox = $("#" + _me.options.checkBoxJson[i].id);

                        if (_me.checkBox.size() < 1) {

                            //把模板变为Zepto DOM对象
                            //判断checkBox是否垂直
                            if (_me.options.isVertical) {
                                var changeStyle = _me._template + '<br>';
                                _me.checkBox = $(changeStyle);
                            } else {
                                _me.checkBox = $(_me._template);
                            }

                            //把html追加到指定容器中
                            $(_me.options.container).append(_me.checkBox);

                            _me.checkBox = $('.one_checkbox').eq(i + 3).find('input').eq(0);
                            //设置ID
                            _me.checkBox.attr("id", _me.options.checkBoxJson[i].id);
                        }
                    }

                    /////设置其他属性
                    for (var i = 0; i < _me.options.checkBoxJson.length; i++) {
                        var CheckBoxInput = $('.one_checkbox').eq(i + 3).find('input').eq(0);
                        _me.setText(CheckBoxInput, _me.options.checkBoxJson[i].text);
                        _me.setParameter(CheckBoxInput, "value", _me.options.checkBoxJson[i].value);
                        _me.setParameter(CheckBoxInput, "checked", _me.options.checkBoxJson[i].checked);
                        _me.setParameter(CheckBoxInput, "disabled", _me.options.checkBoxJson[i].enable);

                        //设置一个name
                        if (!main.options.name) {
                            main.options.name = "checkBox_name_" + (++_name);
                        }
                        _me.setParameter(CheckBoxInput, "name", main.options.name);
                    }
                } else if (util.isString(this.param)) {
                    //作为  选择器  获取  input  集合
                    _me.checkBox = $(this.param).find('input');

                }

                //初始化回调函数
                this.initCallBack();

                //绑定事件处理
                this.bindEvent();

                //执行组件初始化完成回调函数
                this.runCallBack("initFinish", this.options.initFinish);

            },


            /**
             * 初始化回调函数
             */
            initCallBack: function () {

                //初始化完成回调函数
                this.bindCallBack("initFinish", this.options.initFinish);

                //改变选中状态回调事件绑定
                this.bindCallBack("changeSelect", this.options.changeSelect);

                //绑定是否启用回调函数
                this.bindCallBack("changeStatus", this.options.changeStatus);
            },

            /**
             * 绑定回调函数
             * @param name 回调函数名称
             * @param fn 回调函数
             */
            bindCallBack: function (name, fn) {

                //删除原回调函数
                delete this.callBacks[name];

                //如果fn参数类型为function则绑定
                if (util.isFunction(fn)) {
                    this.callBacks[name] = fn;
                }
            },

            /**
             * 执行回调函数
             * @param name 回调函数名称
             * @param context 执行回调函数的上下文(即改变执行函数中this指针)
             * @param params 执行回调函数时传入的参数集合(数组)
             * @returns 回调函数返回的结果
             */
            runCallBack: function (name, context, params) {
                var result;
                if (util.isFunction(this.callBacks[name])) {
                    if (context) {
                        result = this.callBacks[name].apply(context, params);
                    } else {
                        result = this.callBacks[name](params);
                    }
                }
                return result;
            },

            /**
             * 事件绑定
             */
            bindEvent: function () {
                var _me = this;
                if (util.isString(this.param)) {
                    _me.checkBox.off().on("click", function () {
                        _me.runCallBack("changeSelect", this, [this.checked]);
                    });
                } else {
                    _me.checkBox.parent().parent().find("input[name]").off().on("click", function () {
                        _me.runCallBack("changeSelect", this, [this.checked]);
                    });
                }

            },

            setText: function (input, val) { //设置内容
                input.parent().find("span").eq(0).append(val);
            },

            setParameter: function (input, key, val) { //设置参数
                var _me = this;

                if (key == "disabled") {
                    input.prop(key, !val);
                    _me.runCallBack("changeStatus", this, [val]);
                } else if (key == "checked") {
                    input.prop(key, val);
                    _me.runCallBack("changeSelect", this, [val]);
                }
                else {
                    input.prop(key, val);
                }

            },
            /*var input={
             a:"1",
             b:function(){}
             };
             input["a"]="aa";
             var c="b";
             input[c]();*/

            findIndex: function (index) {
                return $('.one_checkbox').eq(index).find('input').eq(0);
            },
            checkBoxIndex: function (index) {
                return index;
            }

        };
        /**
         * 执行组件初始化
         */
        main.init();

        /**
         * 返回对外调用方法对象
         */
        return {
            /**
             * 设置是否允许设置选中状态
             * @param index 索引值
             * @param value bool值,true为允许,false为不允许
             */
            changeEnable: function (index, value) {
                var input = main.findIndex(index);
                main.setParameter(input, "disabled", value);
                return this;
            },

            changeChecked: function (index, value) {
                var input = main.findIndex(index);
                main.checkBoxIndex(index);
                main.setParameter(input, "checked", value);
                return this;
            },

            getStatus: function (index) {
                if (typeof index == "number") {
                    var opt = main.findIndex(index)[0];
                    var text = main.findIndex(index).parent().find("span").eq(0).text();
                    var param = {
                        text: text,
                        name: opt.name,
                        enable: !opt.disabled,
                        checked: opt.checked,
                    };
                    return (param, this);
                } else {
                    var opt = main.checkBox;
                    var test = new Array();
                    var text = '';
                    for (var i = 0; i < opt.length; i++) {
                        text = main.findIndex(i).parent().find("span").eq(0).text();
                        var param = {
                            enable: !opt[i].disabled,
                            checked: opt[i].checked,
                            value: opt[i].value,
                            text: text
                        };
                        test.push(param);
                    }
                    return (text, this);
                }


            },

            /**
             * 改变状态之前回调函数
             * @param fn 组件改变状态之前回调函数
             */
            changeSelect: function (fn) {
                main.bindCallBack("changeSelect", fn);
                return this;
            },

            changeStatus: function (fn) {
                main.bindCallBack("changeStatus", fn);
                return this;
            },

            /**
             * 当前组件版本号
             */
            version: main.version
        }
    }

    //扩展checkBox组件
    exports.checkBox = function (param) {

        if (util.isString(param)) {
            //多个处理不缓存对象
            checkBoxSingle = new checkBox(param);

            return checkBoxSingle;

        } else if (param == undefined || param == "undefined") {
            return false;
        }

        else if (util.isObject(param)) {

            var checkBoxSingle;

            //如果传入值中有ID，则尝试获取缓存对象
            if (param.id) {
                checkBoxSingle = util.getAssemblyCache(_catchPrefix + param.id);
                //如果有缓存对象则返回缓存对象
                if (checkBoxSingle) {
                    return checkBoxSingle;
                }
            }

            //处理组件
            checkBoxSingle = new checkBox(param);

            //把组件对象添加到缓存对象中
            util.addAssemblyCache(_catchPrefix + (param.id || _id), checkBoxSingle);

            //返回当前组件对象
            return checkBoxSingle;
        }

        // return null;
    };

}).call(one, Zepto);
// JavaScript Document

(function ($) {
    this.ui = this.ui || {}; //
    var util = this.util,
        //用变量存储util.js中的方法，方便调用
        exports = this.ui,
        _version = "1.0.0",
        //组件版本号
        _id = 0,
        //id
        _catchPrefix = "ui-collapsible-",
        //组件缓存对象前缀
        _idPrefix = this.namespace + "-collapsible-"; //自定义id前缀

    /**
     * 渲染collapsible
     **/

    function collapsible(param) {

        var main = {

            version: _version,

            options: {
                id: "",
                //id号
                title: "请传标题参数",
                //标题
                isShow: "false",
                //是否展开
                content: "请传内容参数",
                //内容
                align: 'left',
                //对齐方式
                container: 'body',
                //渲染组件容器
            },
            param: param,
            fold: null,

            _template: '<div class="one-collapsible" >' 
							+ '<h4 class="one-collapsible-heading">' 
									+ '<a class="on_change_color">' 
											+ '<span class="one-collapsible_icon_open"></span>'
											+ '<p class="noe_collapsible_title">heading</p>' 
									+ '</a>' 
							+ '</h4>' 
							+ '<div class="one-collapsible-content">'
								 + '<p>这是一个可折叠的展示框。</p>'
						    + '</div>' 
					   + '</div>',

            init: function () {

                var _me = this;

                if (util.isObject(this.param)) {

                    if (util.isObject(this.param)) {
                        $.extend(_me.options, _me.param);
                    }

                    if (!_me.options.id) {
                        _me.fold.attr('id', _idPrefix + (++_id));
                    }

                    //将模板对象变为zeptoz对象
                    _me.fold = $(_me._template);

                    //选取出标题 内容 所在的位置
                    var titlebox = _me.fold.find('p').eq(0); //储存title文字的容器
                    var text = _me.fold.find('p').eq(1); // 储存内容的容器
                    var btn = _me.fold.find('h4').eq(0); //点击区域
                    var contentbox = _me.fold.find('.one-collapsible-content').eq(0); //隐藏显示的模块


                    //如果有title的话，给title赋值
                    if (!_me.options.title === '' && util.isString(_me.options.title)) {
                        $(titlebox).html(_me.options.title);
                    }

                    //如果有内容的话，就把内容赋值上去
                    if (!_me.options.content === '' && util.isString(_me.options.content)) {
                        $(text).html(_me.options.content);
                    }

                    //如果align有值的话，需要为其赋值
                    if (!_me.options.align === '' && util.isString(_me.options.align)) {
                        $(titlebox).css('text-align', _me.options.align);
                    }

                    //main.isShow(true)

                    //把html追加到指定容器中
                    $(_me.options.container).append(_me.fold);

                } else if (util.isString(this.param)) {
                    _me.fold = $(this.param);
                }

					//console.log(_me.fold);
					//alert(_me.fold.children('h4').html())
                _me.fold.children('h4').click(function () {

                    //var tihs=titlebox;
                    console.log(this);

                    if ($(this).siblings().is(':hidden')) {
                        main.show();
                    } else {
                        main.hide();
                    }
                })
            },
            //init结束

            show: function () {

                main.fold.find('.one-collapsible-content').css('display', 'block'); //展开
                main.fold.find('h4').addClass('one_rightangle');
                main.fold.find('span').eq(0).addClass('one-collapsible_icon_close');

            },
            hide: function () {

                main.fold.find('.one-collapsible-content').css('display', 'none'); //关闭
                main.fold.find('h4').removeClass('one_rightangle');
                main.fold.find('span').eq(0).removeClass('one-collapsible_icon_close');
            }

        } ;//main函数结束

        main.init();

        return {
            show: function () {
                main.show();
                return this;
            },
            hide: function () {
                main.hide();
                return this;
            }
        };

    }

    //扩展collapsible组件
    exports.collapsible = function (param) {

        if (util.isString(param)) {
            //string 选择器 #id .class
            return new collapsible(param);

        } else if (util.isObject(param)) {
            var fold;

            //处理组件
            fold = new collapsible(param);

            //返回当前组件对象
            return fold;
        }
        return null;
    }

}).call(one, Zepto);
// JavaScript Document

(function ($) {
    this.ui = this.ui || {}; //
    var util = this.util,
        //用变量存储util.js中的方法，方便调用
        exports = this.ui,
        _version = "1.0.0",
        //组件版本号
        _id = 0,
        //id
        _catchPrefix = "ui-collapsible-",
        //组件缓存对象前缀
        _idPrefix = this.namespace + "-collapsible-"; //自定义id前缀

    /**
     * 渲染collapsible
     **/

    function collapsible(param) {

        var main = {

            version: _version,

            options: {
                id: "",
                //id号
                title: "请传标题参数",
                //标题
                isShow: "false",
                //是否展开
                content: "请传内容参数",
                //内容
                align: 'left',
                //对齐方式
                container: 'body',
                //渲染组件容器
            },
            param: param,
            fold: null,

            _template: '<div class="one_collapsibleset">'
							+'<div class="one-collapsible" >'
								+'<h4 class="one-collapsible-heading">'
									+'<span class="one-collapsible_icon_open"></span>'
									+'<span class="noe_collapsible_title">我是一个标题</span>'
								+'</h4>'
								+'<p class="one-collapsible-content">这是一个可折叠的展示框。</p>'
							+'</div>'
							+'<div class="one-collapsible" >'
								+'<h4 class="one-collapsible-heading">'
									+'<span class="one-collapsible_icon_open"></span>'
									+'<span class="noe_collapsible_title">我是一个标题</span>'
								+'</h4>'
								+'<p class="one-collapsible-content">这是一个可折叠的展示框。</p>'
							+'</div>'
							+'<div class="one-collapsible" >'
								+'<h4 class="one-collapsible-heading">'
									+'<span class="one-collapsible_icon_open"></span>'
									+'<span class="noe_collapsible_title">我是一个标题</span>'
								+'</h4>'
								+'<p class="one-collapsible-content">这是一个可折叠的展示框。</p>'
							+'</div>'
					  +'</div>' ,
			
            init: function () {

                var _me = this;
				
				//如果传进来的值是字符串就处理
                if (util.isString(this.param)) {
                    _me.fold = $(this.param);
                }
				
				//选出操作的按钮及展示框，并为他们加上属性值来判断是否允许展开或关闭
			  	var oChildren_h4 = _me.fold.find('h4');//选出当前下拉列表的数量;
				var oChildren_p = _me.fold.find('.one-collapsible-content'); //当前所有的文字显示框
				oChildren_p.attr({'allowshow':true,'allowhide':true});
				
				//关于布局的第一个子元素的边框需要处理一下。
				oChildren_h4.eq(0).css('border-top','none');
				
				//绑定mouseover事件
				oChildren_h4.mouseover(function(){
					$(this).addClass('one-collapsibleset-heading-hover');
				});
				
				//绑定mouseout事件
				oChildren_h4.mouseout(function(){
					$(this).removeClass('one-collapsibleset-heading-hover');
				});
				
				//绑定click事件
				oChildren_h4.click(function(){
					
					var oChildren_p=$(this).siblings('.one-collapsible-content'); //当前对应的文字框
					var oChildren_icon=$(this).children('.one-collapsible_icon_open').eq(0);
					
					 if(oChildren_p.is(':hidden')&&oChildren_p.attr('allowshow')=='true'){ 
					 		//如果当前隐藏并且允许展开
							//就让当前的展开，将其他兄弟元素的展示框隐藏。
						 oChildren_p.css('display','block').parents('.one-collapsible').siblings().children('.one-collapsible-content').css('display','none');
						 
						   //让当前的图标变换
						   oChildren_icon.addClass('one-collapsible_icon_close').parents('.one-collapsible').siblings().find('.one-collapsible_icon_open').removeClass('one-collapsible_icon_close');
					 }else if(!oChildren_p.is(':hidden')&&oChildren_p.attr('allowhide')=='true'){
						 //如果当前显示并且允许关闭
						 // 就让当前的隐藏
						 oChildren_p.css('display','none');
						 
						 //让当前的图标变换
						  oChildren_icon.removeClass('one-collapsible_icon_close');
					
					}
				})
				
            },//init结束
			
            show: function () {  //全部展开
				
                main.fold.find('.one-collapsible-content').css('display', 'block'); //展开
                main.fold.find('.one-collapsible_icon_open').addClass('one-collapsible_icon_close');

            },
            hide: function () { //全部隐藏

                main.fold.find('.one-collapsible-content').css('display', 'none'); //关闭
                main.fold.find('.one-collapsible_icon_open').removeClass('one-collapsible_icon_close');
				
            },
			allowShow:function(value){ //是否允许展开
				if(util.isBoolean(value)){
					main.fold.find('.one-collapsible-content').attr('allowshow',value);
				}
			},
			allowHide:function(value){  //是否允许关闭
				if(util.isBoolean(value)){
					main.fold.find('.one-collapsible-content').attr('allowhide',value);
				}
				
			},
			allowShowIndex:function(number,value){ //指定eq设置是否允许展开。
				if(util.isNumber(number)&&util.isBoolean(value)){
					main.fold.find('.one-collapsible-content').eq(number).attr('allowshow',value);
				}
			},
			allowHideIndex:function(number,value){
				if(util.isNumber(number)&&util.isBoolean(value)){
					main.fold.find('.one-collapsible-content').eq(number).attr('allowhide',value);
				}
			}
			
			

        }; //main函数结束

        main.init();

        return {
            show: function () { //全部展开
                main.show();
                return this;
            },
            hide: function () { //全部隐藏
                main.hide();
                return this;
            },
			allowShow:function(value){  //是否允许展开
				main.allowShow(value);
                return this;
			},
			allowHide:function(value){  //是否允许关闭
				main.allowHide(value);
                return this;
			},
			allowShowIndex:function(number,value){
				main.allowShowIndex(number,value);
                return this;
			},
			allowHideIndex:function(number,value){
				main.allowHideIndex(number,value);
                return this;
			}
			
        };

    }

    //扩展collapsible组件
    exports.collapsibleset = function (param) {

        if (util.isString(param)) {
            //string 选择器 #id .class
            return new collapsible(param);

        } else if (util.isObject(param)) {
            var fold;

            //处理组件
            fold = new collapsible(param);

            //返回当前组件对象
            return fold;
        }
        return null;
    }

}).call(one, Zepto);
/**
 * dialog
 * @author 程伟凡
 * @version 1.0.1
 * required Zepto.js one.js
 * @modify kui.yang@newtouch.cn 阳葵
 * 修改内容：
 * 1.跳转this.setContent(this.options.content);位置
 * 2.弹出框的位置采用动态方式设置，去除按px方式定位。
 * 3.当标题为空时，删除标题
 * 4.按钮可以自定义添加多个并且可以添加事件
 * 5.增加按钮排放方式的选择
 * 6.增加按钮可以添加自定义class样式名称
 * 7.弹出框关闭后直接remove，不保留
 */
;
(function ($) {

    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version = "1.0.1",//组件版本号
        _id = 0,//id
        _catchPrefix = "ui-dialog-",//组件缓存对象前缀
        _idPrefix = this.namespace + "-dialog-",//自定义id前缀
        events = {};//按钮事件缓存区

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function dialog(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version: _version,

            /**
             * 默认参数
             */
            options: {
                id: "",
                buttons: [
                    {
                        "text": "确认",
                        callback:function(){main.close()}
                    }
                ], //弹出框上的按钮可定制化
                mask: true, //是否有遮罩层
                width: $("body").width() * 0.95 + "px",
                height: "auto",
                top: "35%",
                title: "提示信息",
                content: "",//弹出框内容
                "btn-align":""//多个按钮排放样式，默认为横向，vertical:垂直
            },

            /**
             * 储存初始化参数
             */
            param: param,

            /**
             * 缓存组件对象
             */
            dialog: null,


            /*按钮结构模板*/
            _template: '<div class="one_dialog" style="height: auto; display: none; left: 3%;">'
                + '<div class="one_dialog_title"><h3></h3></div>'
                + '<div class="one_dialog_content"></div>'
                + '<div class="one_dialog_btns"></div>'
                + '</div>',
            _mask: '<div class="one_dialog_mask" style="display:block; "></div>',


            /**
             * 初始化组件函数
             */
            init: function () {
                _me = this;


                //object作为单个对象处理
                if (util.isObject(this.param)) {
                    //设置垂直显示按钮时，默认的top位置
                    if(this.param["btn-align"]=="vertical"&&this.param.buttons.length>1){
                        this.options.top = (35-(this.param.buttons.length-1)*2.5)+"%";
                    }
                    //使用传入的参数覆盖默认参数
                    $.extend(this.options, this.param);

                    //如果未传入id,使用自定义规则生成的ID
                    if (!this.options.id) {
                        this.options.id = this.getId();
                    }

                    //查找组件
                    this.dialog = $("#" + this.options.id);

                    //如果未存在指定id的组件,则创建
                    if (this.dialog.size() < 1) {

                        //把模板变为Zepto DOM对象
                        this.dialog = $(this._template);
                        this.mask = $(this._mask);
                        //把html追加到指定容器中
                        $("body").append(this.dialog);
                        $("body").append(this.mask);

                        //设置ID
                        this.dialog.attr("id", this.options.id);
                    }
                    /*放置于此处，避免在调用one.ui.dialog('info')时，再次执行该方法而导致info内容为空*/
                    this.setContent(this.options.content);//设置内容

                }
                else if (util.isString(this.param)) {
                    this.dialog = $(this._template);
                    this.mask = $(this._mask);
                    //把html追加到指定容器中
                    $("body").append(this.dialog);
                    $("body").append(this.mask);
                    //string作为选择器处理,并且处理多个
                    main.setContent(this.param);
                    main.open();

                }

                this.setTitle(this.options.title);//设置标题

                this.setButtons(this.options.buttons);//设置按钮

                this.setMask(this.options.mask);//设置按钮


                //设置CSS样式
                util.setElementCss(this.dialog, {
                    width: this.options.width,
                    height: this.options.height,
                    top: this.options.top
                });
            },

            getId:function(){
                return  _idPrefix + (++_id);
            },
            setTitle: function (title) { //设置标题
                if (util.isBlank(title)) {//标题未空时删除标题
                    this.dialog.find("div.one_dialog_title").remove();
                } else {
                    this.dialog.find("h3").html(title);
                }
            },
            setContent: function (content) { //设置内容
                this.dialog.find("div.one_dialog_content").eq(0).html(content);
            },
            setButtons: function (buttons) { //设置按钮
                for (var b in buttons) {
                    var button = buttons[b];
                    var text = button["text"];
                    var id = main.getId();
                    var callback=button["callback"]||{};
                    //缓存事件，不能直接绑定，否则前面的按钮事件会被后面的按钮事件替换
                    events[id]=callback;
                    var html = $("<a class=\"one_btn\" id=\""+id+"\">" + text + "</a>");
                    if(this.options["btn-align"]=="vertical"){
                        html = $("<a class=\"one_btn\" id=\""+id+"\" style='display:inherit;'>" + text + "</a>");
                    }
                    var clazz = button["clazz"]||"";
                    if(!util.isBlank(clazz)){
                        html.addClass(clazz);
                    }
                    this.dialog.find("div.one_dialog_btns").append(html);
                    $("#"+id).on("click",function(){
                        var i=$(this).attr("id");//重新获取事件，并触发
                        events[i].call(this.dialog,main);
                    });
                }
            },
            setMask: function (mask) { //设置遮罩层
                if (mask === false) {
                    util.setElementCss(this.mask, {
                        display: "none",
                    });
                }
            },
            open: function (callback) {
                $(this.dialog).show();
            },
            close: function (callback) {
                $(this.dialog).remove();
                $(this.mask).remove();
            },
        };
        /**
         * 执行组件初始化
         */
        main.init();

        /**
         * 返回对外调用方法对象
         */
        return {
            open: function (callback) {
                main.open();
                return this;
            },
            close: function (callback) {
                main.close();
            },
            callBack: function (callback) {
                main.options.callBack = callback;
                return this;
            },
            /**
             * 当前组件版本号
             */
            version: main.version
        };
    }


    //扩展dialog组件one.ui.dialog()
    exports.dialog = function (param) {

        if (util.isString(param)) {

            return new dialog(param);

        } else if (util.isObject(param)) {

            var diaSingle;

            //如果传入值中有ID，则尝试获取缓存对象
            if (param.id) {
                diaSingle = util.getAssemblyCache(_catchPrefix + param.id);
                //如果有缓存对象则返回缓存对象
                if (diaSingle) {
                    return diaSingle;
                }
            }

            //处理组件
            diaSingle = new dialog(param);

            //把组件对象添加到缓存对象中
            util.addAssemblyCache(_catchPrefix + (param.id || _id), diaSingle);

            //默认调用open


            diaSingle.open();


            //返回当前组件对象
            return diaSingle;
        }

        return null;
    };

}).call(one, Zepto);


(function($){


})(one,Zepto);


/**
 * dropMenu
 * @author 程伟凡
 * @version 1.0.0
 * required Zepto.js cpic.js
 */


;
(function($) {

    this.ui = this.ui || {};
    //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
    exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
    _version = "1.0.0", //组件版本号
    _id = 0, //id
    _catchPrefix = "ui-dropMenu-", //组件缓存对象前缀
    _idPrefix = this.namespace + "-dropMenu-";
    //自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function dropMenu(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version : _version,

            /**
             * 默认参数
             */
            options : {
                change:function(info){
                   //info:数组  依次存放  索引  text  value
                }
            },

            /**
             * 储存初始化参数
             */
            param : param,
            
            
            /**
             * 保存  下拉框的信息
             */
            dropMenuInfoArr:[0,'text','value'],
            /**
             * 缓存组件对象
             */
            dropMenu : null,
            
            /**
                  * 回调函数集合
                  */
            callBacks:{},

           
            /*******************************************************************************************/
            /**
             * 初始化组件函数
             */
            init : function() {
                if (util.isString(this.param)) {
                    //作为  选择器  获取  当有多个时 只是获取第一个
                   this.dropMenuBox=$(this.param).eq(0);
                   
                   this.dropMenu=this.dropMenuBox.find('li');
                    
                }
                this.initFunc();
                this.bindEvent();
                
            
           
            },
            /***********************内部函数*********************************************************************/
            
            /**
             * 初始化 函数 (回调函数)
             */
            initFunc:function(){
                //改变状态之前回调事件绑定
                this.bindCallBack("change", this.options.change);
            },

            /**
             * 绑定回调函数
             * @param name 回调函数名称
             * @param fn 回调函数
             */
            bindCallBack : function(name, fn) {
                //删除原回调函数
               delete this.callBacks[name];

                //如果fn参数类型为function则绑定
                if (util.isFunction(fn)) {
                    this.callBacks[name] = fn;
                }
            },
            /**
             * 执行回调函数
             * @param name 回调函数名称
             * @param context 执行回调函数的上下文(即改变执行函数中this指针)【context】
             * @param params 执行回调函数时传入的参数集合(数组)
             * @returns 回调函数返回的结果
             */
            runCallBack : function(name, params,context) {
                var result;
                if (this.callBacks[name]) {
                    if (context) {
                        result = this.callBacks[name].call(context, params);
                    } else {
                        result = this.callBacks[name](params);
                    }
                }
                return result;
            },

            /**
             * 事件绑定
             */
            bindEvent : function() {
                var _me = this;//main
                
                $(main.dropMenuBox).find('input').on("click",function(event) {
                     event.stopPropagation();
                     
                     if($(this).next('ul').is(':visible')){
                     $(this).addClass("one_ui_dropMenu_bottom_corner");
                        $(this).next('ul').hide();
                     }
                     else{
                        $(this).removeClass("one_ui_dropMenu_bottom_corner");
                         $(this).next('ul').show();
                     
                     }
                    
                });
                $(document).on('click',function(){
                    $(main.dropMenuBox).find('input').addClass("one_ui_dropMenu_bottom_corner");
                    $(main.dropMenuBox).find('ul').hide();
                
                });
                // $('.one_radio-button input');
               this.dropMenu.each(function(index,item) {
                    $(item).off('click').on('click',function(event){
                        
                      //调用before回调函数,并且改变回调函数内this指针指向input dom对象,传入参数为改变后的状态值
                        var result=main.runCallBack("before",index,this);
                        
                      //隐藏下拉框
                       $(main.dropMenuBox).find('input').addClass("one_ui_dropMenu_bottom_corner");
                        $(item).parent('ul').hide();
                        
                        //如果事件处理回调函数返回的值为false时阻止本次操作
                        if(util.isBoolean(result)&&!result){
                            return false;
                        }
                        
                        //获取选中的信息。
                      var info=main.dropMenuInfo(index); 
                      main.runCallBack("change",info ,this);
                      
                    
                    });
                });
            },
            dropMenuInfo:function(index){
                var text=this.dropMenu.eq(index).text();
                var value=this.dropMenu.eq(index).attr('value');
                
                $(main.dropMenuBox).find('input').attr('value',text);
                
                return main.dropMenuInfoArr=[index,text,value];
            }
        };
        /**
         * 执行组件初始化
         */
        main.init();
        /*************************************************************/
        /**
         * 返回对外调用方法对象
         */

        return {
          
           getInfo:function(){
               return main.dropMenuInfoArr;
           },
           before:function(fn){
               main.bindCallBack("before",fn);
               return this;
           },
           setMenu:function(index){
               main.dropMenuInfo(index);
               return this;
           },
           change:function(fn){
               main.bindCallBack("change",fn);
               return this;
           },
            /**
             * 当前组件版本号
             */
            version : main.version
        };
    }

    /**************************************扩展confirm组件*************************/
    exports.dropMenu = function(param) {
        var dropMenuSingle = null;
        //如果传入的是string类型,作为选择器处理
        if (util.isString(param)) {
            //多个处理不缓存对象
            dropMenuSingle = new dropMenu(param);

        }else{
            return false;
        } 
     
        return dropMenuSingle;
    };

}).call(one, Zepto);
/**
 * flipSwitch
 * @author 易丙军
 * @version 1.0.0
 * required Zepto.js cpic.js
 */
;(function ($) {
		
	this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
	var util = this.util, //用变量存储util.js中的方法，方便调用
		exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
		_version="1.0.0",//组件版本号
		_id=0,//id
		_catchPrefix="ui-flipSwitch-",//组件缓存对象前缀
		_idPrefix=this.namespace + "-flipSwitch-";//自定义id前缀

	/**
	  * 组件主函数
	  * @param param 组件调用时初始化参数集合
	  */
	function flipSwitch(param)
	{
		//定义组件创建以及事件处理主体对象
		var main={
				/**
				  * 组件版本号
				  */
				version:_version,

				/**
				  * 默认参数
				  */
				options:{
					selector : "", //选择器
					allowOff:true,//是否允许关
					allowOn:true,//是否允许开
					status:"on",//默认状态
					enable:true,//是否启用

					before:function(){},//改变状态之前回调函数,return false可阻止当前操作
					ready:function(){},//组件初始化完成回调函数
					change:function(){}//改变状态时回调函数
				},

				/**
				  * 储存初始化参数
				  */
				param:param,

				/**
				  * 缓存组件对象
				  */
				switchs:null,

				/**
				  * 回调函数集合
				  */
				callBacks:{},

				/**
				  * 组件结构模板
				  */
				_template:'<label class="one_switch">'
			                    +'<input type="checkbox" class="one_switch__input">'
			                    +'<div class="one_switch__toggle"></div>'
		                 +'</label>',

				/**
				  * 初始化组件函数
				  */
				init:function() {
					
					//object作为单个对象处理
					if(util.isObject(this.param)){
						//如果传入进来的是Zepto对象
						if(util.isFunction(this.param.size)){
							this.switchs= this.param;
						}else{
							//使用传入的参数覆盖默认参数
							$.extend(this.options, this.param);

							//如果未传入id,使用自定义规则生成的ID
							if (!this.options.id) {
								this.options.id = _idPrefix + (++_id);
							}

							//查找组件
							this.switchs = $("#"+this.options.id);

							//如果未存在指定id的组件,则创建
							if(this.switchs.size()<1)
							{

								//把模板变为Zepto DOM对象
								this.switchs=$(this._template);

								//把html追加到指定容器中
								$(this.options.container).append(this.switchs); 

								//把switchs变量指向input对象
								this.switchs=this.switchs.find("input").eq(0);

								//设置ID
								this.switchs.attr("id", this.options.id); 
							}

							//设置默认开关状态
							this.setStatus(this.options.status);

							//设置默认启用状态
							this.setEnable(this.options.enable);
						}
					}
					else if(util.isString(this.param)){

						//string作为选择器处理,并且处理多个
						this.switchs=$(param);

					}
							
					//初始化回调函数
					this.initCallBack();

					//绑定事件处理
					this.bindEvent();

					//执行组件初始化完成回调函数
					this.runCallBack("ready",this.options.ready);
				},

				/**
				  * 是否允许改变
				  * @param element 原始dom对象,这里传入input
				  */
				isAllowChange:function(element){
					//如果不允许设置为on或者off时返回false
					if(element.checked&&!this.options.allowOn){
						return false;
					}else if(!element.checked&&!this.options.allowOff){
						return false;
					}
					return true;
				},

				/**
				  * 初始化回调函数
				  */
				initCallBack:function(){

					//绑定组件初始化完成回调函数
					this.bindCallBack("ready",this.options.ready);

					//改变状态之前回调事件绑定
					this.bindCallBack("before",this.options.before);

					//绑定改变状态时回调函数
					this.bindCallBack("change",this.options.change);
				},

				/**
				  * 执行回调函数
				  * @param name 回调函数名称
				  * @param context 执行回调函数的上下文(即改变执行函数中this指针)
				  * @param params 执行回调函数时传入的参数集合(数组)
				  * @returns 回调函数返回的结果
				  */
				runCallBack:function(name,context,params){
					var result;
					if (util.isFunction(this.callBacks[name])) {
						if(context){
							result=this.callBacks[name].apply(context,params);
						}else{
							result=this.callBacks[name](params);
						}
					}
					return result;
				},

				/**
				 * 事件绑定
				 */
				bindEvent:function() {
					var _me=this;
					this.switchs.on("click",function(){
						var flag=_me.isAllowChange(this);
						//如果允许改变为on或者off时，调用对应事件的之前处理函数
						if(flag){

							var status=this.checked?"on":"off";
							//调用before回调函数,并且改变回调函数内this指针指向input dom对象,传入参数为改变后的状态值
							var result=_me.runCallBack("before",this,[status]);

							//如果事件处理回调函数返回的值为false时阻止本次操作
							if(util.isBoolean(result)&&!result){
								return false;
							}

							//调用change回调函数,并且改变回调函数内this指针指向input dom对象,传入参数为改变后的状态值
							_me.runCallBack("change",this,[status]);
						}

						return flag;
					});
				},

				/**
				  * 绑定回调函数
				  * @param name 回调函数名称
				  * @param fn 回调函数
				  */
				bindCallBack:function(name,fn){
					
					//删除原回调函数
					delete this.callBacks[name];

					//如果fn参数类型为function则绑定
					if(util.isFunction(fn)){
						this.callBacks[name]=fn;
					}
				},

				/**
				  * 设置状态
				  * @param status 状态值 on为开off为关
				  */
				setStatus:function(status){
					
					//临时存储main对象,因为在回调函数中this对象指向会改变
					var _me=this;

					this.switchs.each(function(){

						//设置状态
						this.checked=status=="on";

						//调用change回调函数
						_me.runCallBack("change",this,[this.checked?"on":"off"]);

					});
				},
				/**
				  * 得到状态值,默认只获取第一个
				  * @param fn 循环获取当前对象集合值
				  * @returns 状态值 on为开off为关
				  */
				getStatus:function(fn){
					if(util.isFunction(fn)){
						//如果传入的是回调函数,将循环当前switch集合并调用回调函数
						this.switchs.each(function(i){
							fn.call(this,i,this.checked?"on":"off");
						});
					}else{
						return this.switchs[0].checked?"on":"off";
					}
				},
				/**
				  * 设置组件是否启用
				  * @enable bool值,true为启用,false为不启用
				  */
				setEnable:function(enable){
					
					enable=util.convertBool(enable)?false:true;

					this.switchs.each(function(){
						$(this).prop("disabled",enable);
					});
				},
				/**
				  * 设置是否允许设置为off状态
				  * @value bool值,true为允许,false为不允许
				  */
				setAllowOff:function(value){
					main.options.allowOff=util.convertBool(value);
				},
				/**
				  * 设置是否允许设置为on状态
				  * @value bool值,true为允许,false为不允许
				  */
				setAllowOn:function(value){
					main.options.allowOn=util.convertBool(value);
				}
		};

		/**
		 * 执行组件初始化
		 */
		main.init();

		/**
		  * 返回对外调用方法对象
		  */
		return {
			/**
			  * 设置或获取状态
			  * @param p 字符串或者回调函数,字符串on为开off为关,回调函数将循环获取集合值
			  */
			status:function(p){
				if(util.isString(p)){//set
					main.setStatus(p);
				}else if(util.isFunction(p)){//get
					main.getStatus(p);
				}else{//get
					return main.getStatus();
				}
				return this;
			},
			/**
			  * 设置组件是否启用
			  * @param enable bool值,true为启用,false为不启用
			  */
			enable:function(enable){
				main.setEnable(enable);
				return this;
			},
			/**
			  * 设置是否允许设置为on或者off状态
			  * @param key 枚举值 on,off
			  * @param value bool值,true为允许,false为不允许
			  */
			allowStatus:function(key,value){
				if(key=="off"){
					main.setAllowOff(value);
				}else if(key=="on"){
					main.setAllowOn(value);
				}
				return this;
			},
			/**
			  * 改变状态之前回调函数
			  * @param fn 组件改变状态之前回调函数
			  */
			before:function(fn){
				main.bindCallBack("before",fn);
				return this;
			},
			/**
			  * 改变为on状态时回调函数
			  * @param fn 回调函数
			  */
			change:function(fn){
				main.bindCallBack("change",fn);
				return this;
			},
			/**
			  * 获取当前集合对象中switch的数量
			  */
			size:function(){
				return main.switchs.size();
			},
			/**
			  * 获取集合中单个对象
			  * @param index 索引值,以0开始
			  */
			eq:function(index){
				if(main.switchs.size()-1>=index){
					return new flipSwitch(main.switchs.eq(index));
				}
				return null;
			},
			/**
			  * 当前组件版本号
			  */
			version:main.version
		};
	}

	//扩展flipSwitch组件
	exports.flipSwitch = function (param) {

		//如果传入的是string类型,作为选择器处理
		if(util.isString(param)){
			if(/^#/.test(param)){
				param={
					id:param.replace("#","")
				};
			}else{
				//多个处理不缓存对象
				return new flipSwitch(param);
			}
		}
		if(util.isObject(param)){
			
			var switchSingle;

			//如果传入值中有ID，则尝试获取缓存对象
			if (param.id) {
				switchSingle = util.getAssemblyCache(_catchPrefix + param.id);
				//如果有缓存对象则返回缓存对象
				if (switchSingle) {
					return switchSingle;
				}
			}

			//处理组件
			switchSingle = new flipSwitch(param);

			//把组件对象添加到缓存对象中
			util.addAssemblyCache(_catchPrefix +(param.id|| _id), switchSingle);

			//返回当前组件对象
			return switchSingle;
		}

		return null;
	};

}).call(one, Zepto);
/**
 * loading  加载   组件
 *  @提示框的内容；按钮文本自定义
 *  @author wxg
 *  @version 1.0.0
 *  @required Zepto.js cpic.js
 */
;
(function ($) {

    this.ui = this.ui || {};
    //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version = "1.0.0", //组件版本号
        _id = 0, //id
        _catchPrefix = "ui-loading-", //组件缓存对象前缀
        _idPrefix = this.namespace + "-loading-";
    //自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合(用户自定义的   参数设置)
     */
    function loading(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version: _version,

            /**
             * @ 默认参数
             */
            options: {
                /**
                 *@ 不传入时；将动态的设置   数值ID
                 */
                id: "",
                /**
                 *@ 默认为  body ，适用于loading、confirm、dialog等组件
                 * @ 对于input、button、textAera等组件 必须传入父元素
                 */
                container: "body", //渲染的容器

                /**
                 *@  提示框  标题文本
                 */
                title: "",
                /**
                 *@  动画图片
                 */
                loadingGif: "",

                /**
                 *@  提示框  开启
                 */
                open: function () {

                },
                /**
                 *@  提示框  关闭
                 */
                close: function () {

                }
            },

            /**
             * 动画图片  对象  ；预留 目前未使用
             */
            loadingGifs: {
                /**
                 *@ 对应  circle 图标
                 */
                circle: "circle"
            },
            /**
             * 储存初始化参数
             */
            param: param,

            /**
             * 缓存组件对象
             */
            loadings: null,

            /**
             * 回调函数集合
             */
            callBacks: {},

            /**
             * 组件结构模板
             */
            _template: '<div class="one_ui_mask" style="display:none">' + '</div>' + '<div class="one_ui_loading" style="display:none">' + '<div class="one_ui_loading_img"></div><h3></h3>' + '</div>',
            /**********************  初始化函数     **********************************************/
            /**
             * 初始化组件函数
             */
            init: function () {

                //object作为单个对象处理
                /**
                 *@ 主要是  设置  id  同时返回  待处理的 组件对象
                 */
                if (util.isObject(this.param)) {

                    //使用传入的参数覆盖默认参数
                    $.extend(this.options, this.param);

                    //如果未传入id,使用自定义规则生成的ID
                    if (!this.options.id) {
                        this.options.id = _idPrefix + (++_id);
                    }

                    //查找组件
                    this.loadings = $("#" + this.options.id);

                    //如果未存在指定id的组件,则创建
                    if (this.loadings.size() < 1) {

                        //把模板变为Zepto DOM对象
                        this.loadings = $(this._template);

                        //把html追加到指定容器中
                        $(this.options.container).append(this.loadings);

                        //把loadings变量指向one_ui_mask 对象
                        this.mask = $(".one_ui_mask");
                        this.loadings = $(".one_ui_mask").next(".one_ui_loading");

                        //设置ID
                        this.loadings.attr("id", this.options.id);
                    }

                } else if (util.isString(this.param)) {
                    //把模板变为Zepto DOM对象
                    this.loadings = $(this._template);

                    $(".one_ui_mask").next(".one_ui_loading").remove();
                    $(".one_ui_mask").remove();

                    $(this.options.container).append(this.loadings);

                    this.mask = $(".one_ui_mask");
                    this.loadings = $(".one_ui_mask").next(".one_ui_loading");
                    //string作为title处理
                    this.options.title = this.param;

                }

                this.initOption();
                /**
                 * 设置默认的   提示文本
                 */
                if (this.options.text) {

                    main.setText(this.options.text);
                }

            },

            /**********************  内部函数        **********************************************/
            initOption: function () {
                this.setText(this.options.title);
            },
            /**
             * @param text 提示文本
             * 开启 loading  框
             */
            open: function (text) {
                if (text) {
                    this.setText(text);
                }
                $(".one_ui_mask").show();
                $(this.loadings).show();
            },
            /**
             * @param text 提示文本
             * 关闭loading  框
             */
            close: function () {
                $(".one_ui_mask").hide();
                $(this.loadings).hide();
            },
            /**
             * @param text 提示文本
             *
             */
            setText: function (text) {
                $(this.loadings).find("h3").text(text);
            }
        };

        /**
         * 执行组件初始化
         */
        main.init();

        /**
         * 返回对外调用方法对象
         */
        return {
            /**
             * 当前组件版本号
             */
            version: main.version,
            /**
             *
             */
            open: function () {
                main.open();
                return this;
            },
            /**
             *
             */
            close: function () {
                main.close();
                return this;
            }
        };
    }

    //扩展flipSwitch组件
    exports.loading = function (param) {
        var loadingsingle = null;
        //如果传入的是string类型,作为选择器处理
        if (util.isString(param)) {

            //多个处理不缓存对象
            loadingsingle = new loading(param);

        } else if (param === undefined || param === "undefined") {
            loadingsingle = new loading("");
        }
        else if (util.isObject(param)) {

            // var loadingsingle;

            //如果传入值中有ID，则尝试获取缓存对象
            if (param.id) {
                loadingsingle = util.getAssemblyCache(_catchPrefix + param.id);
                //如果有缓存对象则返回缓存对象
                if (!loadingsingle) {
                    //处理组件
                    loadingsingle = new loading(param);

                    //把组件对象添加到缓存对象中
                    util.addAssemblyCache(_catchPrefix + (param.id || _id), loadingsingle);
                }
            }

            //处理组件
            loadingsingle = new loading(param);

            //把组件对象添加到缓存对象中
            util.addAssemblyCache(_catchPrefix + (param.id || _id), loadingsingle);

            //返回当前组件对象
            // return loadingsingle;
        }
        loadingsingle.open();
        return loadingsingle;
    };

}).call(one, Zepto);


/**
 * flipSwitch
 * @author 程伟凡
 * @version 1.0.0
 * required Zepto.js cpic.js
 */
;
(function ($) {

    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version = "1.0.0",//组件版本号
        _id = 0,//id
        _catchPrefix = "ui-flipSwitch-",//组件缓存对象前缀
        _idPrefix = this.namespace + "-flipSwitch-";//自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function navigator(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version: _version,

            /**
             * 默认参数
             */
            options: {
                allowChange: true,
                beforeChange: function () {
                },//切换前回调函数
                afterChange: function () {
                },//切换后回调函数
            },

            /**
             * 储存初始化参数
             */
            param: param,

            /**
             * 缓存组件对象
             */
            navigator: null,

            /**
             * 回调函数集合
             */
            callBacks: {},

            /**
             * 初始化组件函数
             */
            init: function () {
                var _me = this;

                if (util.isString(this.param)) {
                    //作为  选择器  获取  li  集合
                    _me.navigator = $(this.param);

                }

                //初始化回调函数
                this.initCallBack();

                this.runCallBack("initFinish", this.options.initFinish);

                //绑定事件处理
                this.bindEvent();

            },

            isAllowChange: function (element) {
                if (!element) {
                    return false;
                }
                return true;
            },
            /**
             * 初始化回调函数
             */
            initCallBack: function () {

                //初始化完成回调函数
                this.bindCallBack("beforeChange", this.options.beforeChange);

                //改变选中状态回调事件绑定
                this.bindCallBack("afterChange", this.options.afterChange);


            },

            /**
             * 绑定回调函数
             * @param name 回调函数名称
             * @param fn 回调函数
             */
            bindCallBack: function (name, fn) {

                //删除原回调函数
                delete this.callBacks[name];

                //如果fn参数类型为function则绑定
                if (util.isFunction(fn)) {
                    this.callBacks[name] = fn;
                }
            },

            /**
             * 执行回调函数
             * @param name 回调函数名称
             * @param context 执行回调函数的上下文(即改变执行函数中this指针)
             * @param params 执行回调函数时传入的参数集合(数组)
             * @returns 回调函数返回的结果
             */
            runCallBack: function (name, context, params) {
                var result;
                if (util.isFunction(this.callBacks[name])) {
                    if (context) {
                        result = this.callBacks[name].apply(context, params);

                    } else {
                        result = this.callBacks[name](params);
                    }
                }
                return result;
            },

            /**
             * 事件绑定
             */
            bindEvent: function () {
                var _me = this;
                _me.navigator.each(function (index, item) {
                    $(item).find("li").each(function (ind, ite) {
                        $(ite).off().on("click", function () {
                            var flag = _me.isAllowChange(this);
                            if (flag == true) {
                                var status = true;
                            } else {
                                var status = false;
                            }
                            var result = _me.runCallBack("beforeChange", this, [status, index]);

                            //如果事件处理回调函数返回的值为false时阻止本次操作
                            if (util.isBoolean(result) && !result) {
                                return false;
                            }
                            $(item).find("a").prop("class", "");
                            $(item).find("a").eq(ind).prop("class", "one_checked_item");
                            _me.runCallBack("afterChange", null, [status, index]);
                        });
                    });
                });


            },
            changeNavigator: function (index) {
                if ((this.navigator.find("a").eq(index)).length !== 0) {
                    this.navigator.find("a").prop("class", "");
                    this.navigator.find("a").eq(index - 1).prop("class", "one_checked_item");
                }
            },

            removeNavigator: function (string) {
                $(string).find("a").prop("class", "");
                return this;
            },
            /**
             * 获取集合中单个对象
             * @param index 索引值,以0开始
             */
            eq: function (index) {
                if (main.navigator.size() - 1 >= index) {
                    return new flipSwitch(main.navigator.eq(index));
                }
                return null;
            }


        };

        /**
         * 执行组件初始化
         */
        main.init();
        /**
         * 返回对外调用方法对象
         */
        return {
            changeNavigatorTo: function (index) {
                main.changeNavigator(index);
                return this;
            },

            /**
             * 改变状态之前回调函数
             * @param fn 组件改变状态之前回调函数
             */
            beforeChange: function (fn) {
                main.bindCallBack("beforeChange", fn);
                return this;
            },
            clearNavigator: function (string) {
                main.removeNavigator(string);
                return this;
            },
            /**
             * 获取集合中单个对象
             * @param index 索引值,以0开始
             */
            eq: function (index) {
                if (main.navigator.size() - 1 >= index) {
                    return new navigator(main.navigator.eq(index));
                }
                return null;
            },
            /**
             * 当前组件版本号
             */
            version: main.version
        };

    }

    //扩展navigator组件
    exports.navigator = function (param) {
        if (util.isString(param)) {

            return new navigator(param);

        }

        return null;
    };

}).call(one, Zepto);



/**
 * panel
 * @author 易丙军
 * @version 1.0.0
 * required Zepto.js cpic.js
 */
;(function ($) {
		
	this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
	var util = this.util, //用变量存储util.js中的方法，方便调用
		exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
		_version="1.0.0",//组件版本号
		_id=0,//id
		_catchPrefix="ui-panel-",//组件缓存对象前缀
		_idPrefix=this.namespace + "-panel-";//自定义id前缀

	/**
	  * 组件主函数
	  * @param param 组件调用时初始化参数集合
	  */
	function panel(param)
	{
		//定义组件创建以及事件处理主体对象
		var main={
				/**
				  * 组件版本号
				  */
				version:_version,

				/**
				  * 打开时追加的className
				  */
				openClass:"one_panel_open",
				/**
				  * 遮罩层的className
				  */
				maskClass:"one_panel_mask",
				/**
				  * 显示className
				  */
				blockClass:"one_panel_block",
				/**
				  * left className
				  */
				leftClass:"one_panel_left",
				/**
				  * right className
				  */
				rightClass:"one_panel_right",
				/**
				  * content className
				  */
				panelContentClass:"one_panel_content",

				/**
				  * 默认参数
				  */
				options:{
					id : "",
					content:"",//内容

					before:function(){},//显示和隐藏之前回调函数,return false可阻止当前操作
					change:function(){}//显示和隐藏之后回调函数
				},

				/**
				  * 储存初始化参数
				  */
				param:param,

				/**
				  * 缓存组件对象
				  */
				panels:null,

				/**
				  * 遮罩层组件缓存对象
				  */
				mask:null,
				
				/**
				  * 回调函数集合
				  */
				callBacks:{},

				/**
				  * 组件结构模板
				  */
				_template:'<div class="one_panel one_panel_animate">'
								+'<div class="one_panel_content"></div>'
						   +'</div>',

				/**
				  * 组件遮罩层结构模板
				  */
				_maskTemplate:'<div class="one_panel_mask"></div>',

				/**
				  * 初始化组件函数
				  */
				init:function() {
					
					//object作为单个对象处理
					if(util.isObject(this.param)){
						//如果传入进来的是Zepto对象
						if(util.isFunction(this.param.size)){
							this.panels= this.param;
						}else{
							//使用传入的参数覆盖默认参数
							$.extend(this.options, this.param);

							//如果未传入id,使用自定义规则生成的ID
							if (!this.options.id) {
								this.options.id = _idPrefix + (++_id);
							}

							//查找组件
							this.panels = $("#"+this.options.id);

							//如果未存在指定id的组件,则创建
							if(this.panels.size()<1)
							{
								//把模板变为Zepto DOM对象
								this.panels=$(this._template);

								this.panels.children("."+this.panelContentClass).append(this.options.content);

								//把html追加到指定容器中
								$("body").append(this.panels);

								//设置ID
								this.panels.attr("id", this.options.id); 
							}
						}
					}
					else if(util.isString(this.param)){

						//string作为选择器处理,并且处理多个
						this.panels=$(param);

					}
							
					//初始化回调函数
					this.initCallBack();

					//绑定事件处理
					this.bindCloseEvent();

					//执行组件初始化完成回调函数
					this.runCallBack("ready",this.options.ready);
				},

				/**
				  * 初始化回调函数
				  */
				initCallBack:function(){

					//绑定组件初始化完成回调函数
					this.bindCallBack("ready",this.options.ready);

					//改变状态之前回调事件绑定
					this.bindCallBack("before",this.options.before);

					//绑定改变状态时回调函数
					this.bindCallBack("change",this.options.change);
				},

				/**
				  * 执行回调函数
				  * @param name 回调函数名称
				  * @param context 执行回调函数的上下文(即改变执行函数中this指针)
				  * @param params 执行回调函数时传入的参数集合(数组)
				  * @returns 回调函数返回的结果
				  */
				runCallBack:function(name,context,params){
					var result;
					if (util.isFunction(this.callBacks[name])) {
						if(context){
							result=this.callBacks[name].apply(context,params);
						}else{
							result=this.callBacks[name](params);
						}
					}
					return result;
				},

				/**
				  * 绑定回调函数
				  * @param name 回调函数名称
				  * @param fn 回调函数
				  */
				bindCallBack:function(name,fn){
					
					//删除原回调函数
					delete this.callBacks[name];

					//如果fn参数类型为function则绑定
					if(util.isFunction(fn)){
						this.callBacks[name]=fn;
					}
				},

				/**
				 * 绑定遮罩层点击事件
				 */
				bindCloseEvent:function() {
					if(this.mask){
						var _me=this;
						this.mask.off().on("click",function(){
							_me.close(this);
						});
					}
				},

				/**
				 * 打开
				 */
				open:function(position) {

					//默认从左边打开
					position=position||"left";

					var _me=this,
						domHeight=$(document).height(),
						winTop=$(window).scrollTop();

					//循环打开panel
					this.panels.each(function(){

						//先调用before回调函数
						var result=_me.runCallBack("before",this,["open",position]);

						//如果事件处理回调函数返回的值为false时阻止本次操作
						if(!util.isBoolean(result)||result){

							var _$this=$(this);

							//移除left和right样式
							_$this.removeClass(_me.leftClass+" "+_me.rightClass)
									//设置当前左或右
								  .addClass(position=="left"?_me.leftClass:_me.rightClass)
									//追加遮罩层
								  .after(_me._maskTemplate);

							//计算panel的高度,高度计算时需要减掉panel本身padding-top，bottom的值
							var panelh=domHeight-parseInt(_$this.css("padding-top"))-parseInt(_$this.css("padding-bottom"));
							//设置panel高度和计算内容距离顶部的值(取滚动条位置)
							_$this.height(panelh)
								  .addClass(_me.blockClass)
								  .children("."+_me.panelContentClass)
								  .css("margin-top",winTop);

							//如果立即追加class动画效果无效
							setTimeout(function(){
								_$this.addClass(_me.openClass);	
							},10);

							//调用change回调函数
						    _me.runCallBack("change",this,["open",position]);
						}


					});

					//缓存所有遮罩层对象
					this.mask=$("."+this.maskClass);

					//绑定遮罩层点击事件
					this.bindCloseEvent();

					//显示遮罩层
					this.mask.show();
				},

				/**
				 * 关闭
				 */
				close:function(o) {

					var _me=this;

					function closePanel(callC,mask,params)
					{
						//获取当前位置
						position=callC.hasClass(_me.leftClass)?"left":"right";
						params.push(position);

						//先调用before回调函数
						var result=_me.runCallBack("before",callC[0],params);

						if(!util.isBoolean(result)||result){

							//处理动画完成事件
							function handler(){
								//隐藏panel
								$(this).removeClass(_me.blockClass);

								//移除事件监听
								// Chrome, Safari, Opera
								this.removeEventListener("webkitTransitionEnd",arguments.callee, false);
								// 标准语法
								this.removeEventListener("transitionend",arguments.callee, false);
							}

							// Chrome, Safari, Opera
							callC[0].addEventListener("webkitTransitionEnd",handler, false);
							// 标准语法
							callC[0].addEventListener("transitionend",handler, false);

							//关闭panel
							callC.removeClass(_me.openClass);
							//移除遮罩层
							mask.hide().remove();
							//调用change回调函数
						    _me.runCallBack("change",callC[0],params);
						}
					}

					//如果传入遮罩层对象
					if(util.isString(o)){
						//传入字符串当做选择器处理
						$(o).each(function(){
							closePanel($(this),$(this).next(),["close"]);
						});
					}else{
						var callC,
							mask;
						if(util.isObject(o)){
							//传入原生div对象处理(遮罩层对象)
							mask=$(o);
							callC=mask.prev();
						}else if(util.isNumber(o)){
							//传入索引值处理
							mask=$("."+this.maskClass).eq(o);
							callC=mask.prev();
						}else if(util.isUndefined(o)){
							//未传入任何值,默认获取当前集合中panel显示的第一个
							this.panels.each(function(){
								callC=$(this);
								mask=callC.next();
								if(callC.hasClass(_me.openClass)){
									return false;
								}
							});
							if(!callC||!mask){
								return;
							}
						}else{
							return;
						}
						closePanel(callC,mask,["close"]);
					}
				}
		};

		/**
		 * 执行组件初始化
		 */
		main.init();

		/**
		  * 返回对外调用方法对象
		  */
		return {
			/**
			  * 改变状态之前回调函数
			  * @param fn 组件改变状态之前回调函数
			  */
			before:function(fn){
				main.bindCallBack("before",fn);
				return this;
			},
			/**
			  * 改变为on状态时回调函数
			  * @param fn 回调函数
			  */
			change:function(fn){
				main.bindCallBack("change",fn);
				return this;
			},
			/**
			  * 打开panel
			  */
			open:function(position){
				main.open(position);
				return this;
			},
			/**
			  * 关闭panel
			  */
			close:function(){
				ar=arguments;
				main.close(ar.length>0?ar[0]:undefined);
				return this;
			},
			/**
			  * 获取当前集合对象中panel的数量
			  */
			size:function(){
				return main.panels.size();
			},
			/**
			  * 获取集合中单个对象
			  * @param index 索引值,以0开始
			  */
			eq:function(index){
				if(main.panels.size()-1>=index){
					return new panel(main.panels.eq(index));
				}
				return null;
			},
			/**
			  * 当前组件版本号
			  */
			version:main.version
		};
	}

	//扩展panel组件
	exports.panel = function (param) {

		//如果传入的是string类型,作为选择器处理
		if(util.isString(param)){
			if(/^#/.test(param)){
				param={
					id:param.replace("#","")
				};
			}else{
				//多个处理不缓存对象
				return new panel(param);
			}
		}
		if(util.isObject(param)){
			
			var panelsSingle;

			//如果传入值中有ID，则尝试获取缓存对象
			if (param.id) {
				panelsSingle = util.getAssemblyCache(_catchPrefix + param.id);
				//如果有缓存对象则返回缓存对象
				if (panelsSingle) {
					return panelsSingle;
				}
			}

			//处理组件
			panelsSingle = new panel(param);

			//把组件对象添加到缓存对象中
			util.addAssemblyCache(_catchPrefix +(param.id|| _id), panelsSingle);

			//返回当前组件对象
			return panelsSingle;
		}

		return null;
	};

}).call(one, Zepto);
/**
 * flipSwitch
 * @author 程伟凡
 * @version 1.0.1
 * required Zepto.js cpic.js
 * @modify kui.yang@newtouch.cn
 * 1.解决点击raido时不触发事件的问题
 * 2.统一使用change方法名
 */
;
(function($) {

    this.ui = this.ui || {};
    //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
    exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
    _version = "1.0.0", //组件版本号
    _id = 0, //id
    _name=0,  //radioName
    _catchPrefix = "ui-radioButton-", //组件缓存对象前缀
    _idPrefix = this.namespace + "-radioButton-";
    //自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function radioButton(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version : _version,

            /**
             * 默认参数
             */
            options : {
                radioJson:[{text:"text",value:"text",check:true,r_disnable:true},
                           ],
                layout:"Horizontal",//vertical
                name:"",
                container:".effectShow",
                setChenck:function(index){
                    
                },
                setDisable:function(index){
                    
                },
                getInfo:function(index){
                    
                },
                ready : function(txt) {
                    console.log(txt);
                },
                change:function(radioInfo){
                    //console.log("索引、text、value、check、disable ||"+radioInfo.index+"|"+radioInfo.text+"|"+radioInfo.value+"|"+radioInfo.check+"|"+radioInfo.r_disable);
                    //return radioInfo;
                }
            },

            /**
             * 储存初始化参数
             */
            param : param,
            
            /**
             * 
             */
            radioInfoArr:{},
            /**
             * 获取页面中已经存在的  radio数目
             */
            radioNum:0,
            /**
             * 缓存组件对象
             */
            radioButton : null,
            
            /**
                  * 回调函数集合
                  */
            callBacks:{},

            /*按钮结构模板*/
            _template : '<label class="one_radio-button">'
                              +'<input type="radio" disabled="" >'
                              +'<div class="one_radio-button__checkmark"></div>'
                              +'<span>radiotext</span>'
                          +'</label>',
            /*******************************************************************************************/
            /**
             * 初始化组件函数
             */
            init : function() {

                //object作为单个对象处理
                if (util.isObject(this.param)) {

                    //使用传入的参数覆盖默认参数
                    $.extend(this.options, this.param);
                    this.initOption();
                   

                } else if (util.isString(this.param)) {
                    //作为  选择器  获取  input  集合
                   this.radioButton=$(this.param).find('input');
                    
                }
                this.initFunc();
                this.bindEvent();
                
                this.runCallBack("ready","初始化完毕");
           
            },
            /***********************内部函数*********************************************************************/
            /**
             *初始化  参数。主要是 生成对象时 
             */
            initOption : function() {

                this.getRadioNum();
                this.addTemplate(this.options.radioJson);
                
                //重定向；缓存  input  集合
                this.radioButton=$('.'+this.options.labelClass).find('input');
                this.setOptions(this.options.radioJson);
            },
            /**
             * 初始化 函数 (回调函数)
             */
            initFunc:function(){
                //绑定组件初始化完成回调函数
                this.bindCallBack("ready", this.options.ready);

                //改变状态之前回调事件绑定
                this.bindCallBack("change", this.options.change);
            },

            /**
             * 绑定回调函数
             * @param name 回调函数名称
             * @param fn 回调函数
             */
            bindCallBack : function(name, fn) {

                //删除原回调函数
               delete this.callBacks[name];

                //如果fn参数类型为function则绑定
                if (util.isFunction(fn)) {
                    this.callBacks[name] = fn;
                }
            },
            /**
             * 执行回调函数
             * @param name 回调函数名称
             * @param context 执行回调函数的上下文(即改变执行函数中this指针)【obj】
             * @param params 执行回调函数时传入的参数集合(数组)
             * @returns 回调函数返回的结果
             */
            runCallBack : function(name, params,obj) {
                var result;
                if (this.callBacks[name]) {
                    if (obj) {
                        result = this.callBacks[name].call(obj, params);
                    } else {
                        result = this.callBacks[name](params);
                    }
                }
                return result;
            },

            /**
             * 事件绑定
             */
            bindEvent : function() {
                var _me = this;//main
                // $('.one_radio-button input');
               this.radioButton.each(function(index,item) {
                    $(item).off('click').on('click',function(){
                        //保存   checked  信息
                      var info=  main.radioInfo(index);
                       
                      main.runCallBack("change",main.radioInfoArr,this);
                      
                     // alert("change"+index);
                    //  console.log(info);
                    });
                });
            },
            /**
             *  获取页面中  已经存在的   radio  数目 
             */
            getRadioNum:function(){
                this.radioNum=$('.one_radio-button').length;
            },
            
            /**
             * 功能：1、添加 radio模板。2、给每一个radio设置id(如果没有的话).3、给一组radio添加一个相同的类。
             * @Pramas: radioJsonArr:   this.options.radioJson
             * 
             */
            addTemplate:function (radioJsonArr) {
                //遍历radio数组对象的值；
                //var radioJsonArr=this.options.radioJson;
                for (var i=0; i < radioJsonArr.length; i++) {
                    //如果未传入id,使用自定义规则生成的ID
                    if (!radioJsonArr[i].id) {
                        radioJsonArr[i].id = _idPrefix + (++_id);
                    }

                    //查找组件
                    this.radioButton = $("#" + radioJsonArr[i].id);

                    //如果未存在指定id的组件,则创建
                    if (this.radioButton.size() < 1) {

                        //把模板变为Zepto DOM对象
                        this.radioButton = $(this._template);
                        
                        $(this.options.container).append(this.radioButton);
                        //this.radiobutton重定向
                        //
                        this.radioButton = $('.one_radio-button').eq(i+this.radioNum).find('input').eq(0);
                        //设置ID
                        this.radioButton.attr("id", radioJsonArr[i].id);
                    }
                    // 添加  类（一组radio添加相同的类）
                    if (!this.options.labelClass) {
                        this.options.labelClass="labelClass"+(++_name);
                    }
                     this.radioButton.parent().addClass(this.options.labelClass);
                }
 
            },
            setOptions:function(radioJsonArr){
                   /////设置其他属性
              for (var i=0; i < radioJsonArr.length; i++) {
                    //var radioInput = this.getRadioInput(i);
                    
                    //设置一个name
                    if (!this.options.name) {
                        this.options.name="radio_name_"+(++_name);
                    }
                      this.radioName(i,"name",this.options.name);
                   // radioInput.attr("value",radioJsonArr[i].value);
                    
                    this.radioText(i,"text",radioJsonArr[i].text);
                    this.radioValue(i,"value",radioJsonArr[i].value);
                    this.radioDisable(i,"disabled",radioJsonArr[i].r_disable);
                    this.radioCheck(i,"checked",radioJsonArr[i].check);
                   
                   if (radioJsonArr[i].check) {
                       this.radioInfo(i);
                   }
                    
                  
              }
        
            },
                 
            radioText:function(index,key,val){
                $(this.radioButton[index]).eq(0).next().next('span').text(val);
            },
            radioValue:function(index,key,val){
                $(this.radioButton[index]).eq(0).attr(key,val);
            },
            radioCheck:function(index,key,val){
		
                if(val){
					//$(this.radioButton).removeAttr(key);
                    $(this.radioButton[index]).eq(0).prop(key,val);
                }else{
                    //input[key]=false;
                    $(this.radioButton[index]).eq(0).prop(key,val);
                }
                    
                
            },
            radioDisable:function(index,key,val){
                if(val){
                     $(this.radioButton[index]).eq(0).prop(key,val);
                }else{
                   // input[key]=false;
                    $(this.radioButton[index]).eq(0).prop(key,val);
                }
            },
            radioName:function(index,key,val){
                 $(this.radioButton[index]).eq(0).attr(key,val);
            },
            radioInfo:function(index){
              return this.radioInfoArr={
                       index:index,
                       text:$(this.radioButton[index]).next().next('span').eq(0).text(),
                       value:this.radioButton[index].value,
                       r_disable:this.radioButton[index].disabled,
                       check:this.radioButton[index].checked
                       };
                    // r_disnable:(function(b_val){if (b_val=="undefined") {
                       // return false;
                   // } else{
                       // return b_val;
                   // }})(this.radioButton[index].disabled),  
            }
          
        };
        /**
         * 执行组件初始化
         */
        main.init();
        /*************************************************************/
        /**
         * 返回对外调用方法对象
         */

        return {
           setDisable:function(index,b_val){
              // main.getRadioInput(index);
               main.radioDisable(index,"disabled",b_val);
			   main.radioInfo(index);
			   return this;
           },
           setCheck:function(index,b_val){
               main.radioCheck(index,"checked",b_val);
               main.radioInfo(index);
			   return this;
           },
           getInfo:function(){
             return main.radioInfoArr;
           },
		   change:function(fn){
		     main.bindCallBack("click",fn);
		   
		   },
            /**
             * 当前组件版本号
             */
            version : main.version
        };
    }

    /**************************************扩展confirm组件*************************/
    exports.radioButton = function(param) {
        var radioButtonSingle = null;
        //如果传入的是string类型,作为选择器处理
        if (util.isString(param)) {
            //多个处理不缓存对象
            radioButtonSingle = new radioButton(param);

        }else if(param==undefined||param=="undefined"){
            return false;
        } 
        else if (util.isObject(param)) {
            //如果传入值中有ID，则尝试获取缓存对象
            if (param.id) {
                radioButtonSingle = util.getAssemblyCache(_catchPrefix + param.id);
                //如果有缓存对象则返回缓存对象
                if (!radioButtonSingle) {

                    radioButtonSingle = new radioButton(param);

                    //把组件对象添加到缓存对象中
                    util.addAssemblyCache(_catchPrefix + (param.id || _id), radioButtonSingle);
                }
            } else {
                //处理组件
                radioButtonSingle = new radioButton(param);

                //把组件对象添加到缓存对象中
                util.addAssemblyCache(_catchPrefix + (param.id || _id), radioButtonSingle);
            }
        }
        // radioButtonSingle.open();
        return radioButtonSingle;
    };

}).call(one, Zepto);
/**
 * slider
 * @author 易丙军
 * @version 1.0.1
 * required Zepto.js one.js
 * @modify kui.yang@newtouch.cn
 * 1.放开toggle方法，供外部调用使用
 * 2.增加是否允许循环标示
 * 3.解决滑动到第一个页面时，上一个页面未隐藏的问题
 * 3.解决当不显示分页信息时，页面报js异常的问题
 *
 */
;(function ($) {

    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version="1.0.1",//组件版本号
        _id=0,//id
        _catchPrefix="ui-slider-",//组件缓存对象前缀
        _idPrefix=this.namespace + "-slider-";//自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function slider(panel,param)
    {
        var main={

            /**
             * 组件版本号
             */
            version:_version,

            /**
             * 默认参数
             */
            options:{
                animationSpeed: 500, //切换动画耗时 ms
                advanceSpeed: 5000, //自动切换时间 ms
                auto: true, //是否自动播放
                loop:true,//是否循环滚动
                direction:"next",//切换方向 next,prev
                toggleMode:"slide",//切换方式,目前支持 fade,slide
                pagination:true,//是否显示分页
                paginationMode:"bullets",//分页模式 目前支持 bullets

                toggleBefore:function(index){},//切换前回调函数。index为数组,长度为2。index[0]表示当前显示的索引,index[1]表示切换至指定索引
                toggleAfter:function(index){},//切换后回调函数,index表示当前显示的索引值
                ready:function(){}//组件初始化完成回调函数
            },

            /**
             * 回调函数集合
             */
            callBacks:{},

            /**
             * 轮播图结构对象
             */
            panel:panel,

            /**
             * 轮播内容对象集合
             */
            sliders:null,

            /**
             * 圆点对象集合
             */
            bullets:null,

            /**
             * 当前显示的索引对象
             */
            activeIndex:0,

            /**
             * 轮播内容总数量
             */
            activeCount:0,

            /**
             * 自动播放定时器对象
             */
            autoInterval:null,

            /**
             * 锁
             */
            locked:false,

            /**
             * 初始化
             */
            init:function(){

                //使用传入的参数覆盖默认参数
                $.extend(this.options, param);

                //获取轮播内容集合
                this.sliders=this.panel.find(".one_slider_toggle");

                //获取轮播内容总数量
                this.activeCount=this.sliders.size();

                //默认显示第一张
                this.sliders.eq(this.activeIndex).css({"z-index" : 3});

                //自动切换
                this.autoToggle();

                //初始化分页
                this.initPagination();

                //初始化回调函数
                this.initCallBack();

                //执行组件初始化完成回调函数
                this.runCallBack("ready");
                this.bindSwipe(this.options["selector"],this.options["toggleAfter"]);
            },

            /**
             * 初始化分页
             */
            initPagination:function(){
                if(this.options.pagination){
                    this.initBullets();
                }else{
                    this.panel.css("padding-bottom","0");
                }
            },

            /**
             * 初始化圆点图片
             */
            initBullets:function(){
                if(this.options.paginationMode=="bullets"){
                    this.bullets=this.panel.find(".one_slider_bullets");
                    if(this.bullets.size()>0){
                        var li=[];
                        for (var i=0;i<this.activeCount;i++) {
                            li.push('<li'+(i==0?' class="current"':'')+' index="'+i+'"><a href="javascript:;"></a></li>');
                        }

                        this.bullets.find("ul").html(li.join(""));

                        //缓存圆点对象(所有li)
                        this.bullets=this.bullets.find("li");

                        //圆点图片点击事件绑定
                        this.bindBulletsClick();
                    }
                }
            },

            /**
             * 圆点图片点击事件绑定
             */
            bindBulletsClick:function(){
                var _me=this;
                this.bullets.click(function(){
                    //清除定时器
                    _me.clearAuto();

                    //切换到指定索引值
                    _me.toggle(util.convertInt($(this).attr("index")),function(){

                        //开始自动切换
                        _me.autoToggle();

                    });
                });
            },

            /**
             * 设置当前显示的图片对应圆点样式
             */
            setActiveBullet:function(){
                if(this.options.paginationMode=="bullets"){
                    this.bullets.removeClass("current")
                        .eq(this.activeIndex)
                        .addClass("current");
                }
            },

            /**
             * 自动切换
             */
            autoToggle:function(){

                var _me=this;

                //如果自动播放
                if(this.options.auto) {

                    //先清除定时器
                    this.clearAuto();

                    //设定自动播放定时器对象
                    this.autoInterval = setInterval(function(){
                        //调用切换方法
                        _me.toggle(_me.options.direction);

                    },this.options.advanceSpeed);
                }
            },

            /**
             * 清除定时器
             */
            clearAuto:function(){
                clearInterval(this.autoInterval);
            },

            /**
             * 解锁
             */
            unlock:function(){
                this.locked = false;
            },

            /**
             * 锁
             */
            lock:function(){
                this.locked = true;
            },

            /**
             * 得到切换至索引值
             * @param active 索引值或者方向
             */
            getToggleToIndex:function(active){
                var result=active;
                if(active === "next") {
                    result=this.activeIndex+1;
                    if(result === this.activeCount) {
                        result = 0;
                    }
                } else if(active === "prev") {
                    result=this.activeIndex-1;
                    if(result < 0) {
                        result = this.activeCount-1;
                    }
                }

                return result;
            },

            /**
             * 切换
             * @param active 索引值或者方向
             * @param callback 切换完成回调函数
             */
            toggle:function(active,callback){
                var _me=this;

                //当前显示图片的索引值
                var prevactiveIndex = this.activeIndex;

                //切换方向
                var slideDirection = active;
                //exit function if bullet clicked is same as the current image
                if(prevactiveIndex === slideDirection) { return false; }

                //切换前回调函数
                var result=this.runCallBack("toggleBefore",null,[this.activeIndex,this.getToggleToIndex(active)]);

                //如果切换前回调函数返回的值为false时阻止本次操作
                if(util.isBoolean(result)&&!result){
                    return false;
                }

                //reset Z & Unlock
                function resetAndUnlock() {

                    util.isFunction(callback)&&callback();

                    _me.sliders.eq(prevactiveIndex).css({"z-index" : 1});
                    _me.unlock();

                    //切换后回调函数
                    _me.runCallBack("toggleAfter",null,_me.activeIndex);
                }

                var loop = this.options["loop"];
                if(!this.locked) {
                    this.lock();
                    var go2continue = true;//是否继续
                    //deduce the proper activeIndex);
                    if(active === "next") {
                        this.activeIndex++;
                        if(this.activeIndex === this.activeCount) {
                            if(!loop){//不循环的情况下，保持原有的下标不变
                                go2continue=false;
                                this.activeIndex--;
                            }else{
                                this.activeIndex = 0;
                            }

                        }
                    } else if(active === "prev") {
                        this.activeIndex--;
                        if(this.activeIndex < 0) {
                            if(!loop){
                                this.activeIndex++;
                                go2continue=false;
                            }else{
                                this.activeIndex = this.activeCount-1;
                            }
                        }
                    } else {
                        this.activeIndex = active;
                        if (prevactiveIndex < this.activeIndex) {
                            slideDirection = "next";
                        } else if (prevactiveIndex > this.activeIndex) {
                            slideDirection = "prev"
                        }
                    }
                    if(!go2continue){
                        this.unlock();
                        return;
                    }

                    //set to correct bullet
                    if(this.options["pagination"]){
                        this.setActiveBullet();
                    }

                    this[this.options.toggleMode+"Toggle"](slideDirection,prevactiveIndex,resetAndUnlock);
                }
            },

            /**
             * 渐隐切换
             */
            fadeToggle:function(slideDirection,prevactiveIndex,callback){
                this.sliders.eq(prevactiveIndex).css({"z-index" : 2});
                this.sliders.eq(this.activeIndex).css({"opacity" : 0, "z-index" : 3})
                    .animate({"opacity" : 1}, this.options.animationSpeed, callback);
            },

            /**
             * 滑动切换
             */
            slideToggle:function(slideDirection,prevactiveIndex,callback){

                var wd=this.sliders.eq(this.activeIndex).parent().parent().width(),
                    left=wd*(slideDirection=="next"?1:-1),
                    left2=wd*-1*(slideDirection=="next"?1:-1);
                this.sliders.eq(prevactiveIndex).siblings().hide(); //隐藏当前轮换图所有兄弟节点
                var activeSlider = this.sliders.eq(this.activeIndex);
                var prevSlider = this.sliders.eq(prevactiveIndex);
                activeSlider.css({ "z-index": 2, "left": left, "display": "block" }).animate({ "left": 0 }, this.options.animationSpeed, function() {
                    prevSlider.hide();//隐藏上一个页面
                    callback();

                }); //设置需要显示的下一张图片位置，（如果是上一张就定位在当前图最右边。如果是下一张就定位在当前图最左边）然后执行动画

                prevSlider.css({ "z-index": 3, "display": "block" }).animate({ "left": left2 }, this.options.animationSpeed, function() {
                    console.log(prevactiveIndex);
                }); //如果是上一张就把当前显示图片往左边移动。如果是下一张就当前显示图片往右边移动

            },

            /**
             * 绑定回调函数
             * @param name 回调函数名称
             * @param fn 回调函数
             */
            bindCallBack:function(name,fn){

                //删除原回调函数
                delete this.callBacks[name];

                //如果fn参数类型为function则绑定
                if(util.isFunction(fn)){
                    this.callBacks[name]=fn;
                }
            },

            /**
             * 初始化回调函数
             */
            initCallBack:function(){

                //绑定组件初始化完成回调函数
                this.bindCallBack("ready",this.options.ready);

                //改变状态之前回调事件绑定
                this.bindCallBack("toggleBefore",this.options.toggleBefore);

                //绑定改变状态时回调函数
                this.bindCallBack("toggleAfter",this.options.toggleAfter);
            },

            /**
             * 执行回调函数
             * @param name 回调函数名称
             * @param context 执行回调函数的上下文(即改变执行函数中this指针)
             * @param params 执行回调函数时传入的参数集合(数组)
             * @returns 回调函数返回的结果
             */
            runCallBack:function(name,context,params){
                var result;
                if (util.isFunction(this.callBacks[name])) {
                    if(context){
                        result=this.callBacks[name].apply(context,params);
                    }else{
                        result=this.callBacks[name](params);
                    }
                }
                return result;
            },
            /**
             * 绑定手势左右滑动时，切换效果
             * @param target
             * @param callBack
             */
            bindSwipe:function(target,callBack){
                $(target).on("swipeLeft",function(){
                    main.toggle("next",callBack);
                }).on("swipeRight",function(){
                    main.toggle("prev",callBack);
                });
            }
        };

        main.init();

        return {
            /**
             * 切换前回调函数
             * @param fn 回调函数
             */
            toggleBefore:function(fn){
                main.bindCallBack("toggleBefore",fn);
                return this;
            },
            /**
             * 切换后回调函数
             * @param fn 回调函数
             */
            toggleAfter:function(fn){
                main.bindCallBack("toggleAfter",fn);
                return this;
            },
            toggle:function(slideDirection,callback){
                main.toggle(slideDirection,callback);
            },
            /**
             * 当前组件版本号
             */
            version:main.version
        };
    }

    //扩展panel组件
    exports.slider = function (param) {

        //如果传入的是string类型,作为选择器处理
        if(util.isString(param)){
            param={
                selector:param
            };
        }
        if(util.isObject(param)){

            var sliders=[];

            $(param.selector).each(function(){
                sliders.push(new slider($(this),param));
            });

            //返回当前组件对象
            if(sliders.length>0){
                return sliders.length>1?sliders:sliders[0];
            }
        }

        return null;
    };

}).call(one, Zepto);
/**
 * flipSwitch
 * @author 程伟凡
 * @version 1.0.0
 * required Zepto.js cpic.js
 */
;(function ($) {
        
    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version="1.0.0",//组件版本号
        _id=0,//id
        _catchPrefix="ui-flipSwitch-",//组件缓存对象前缀
        _idPrefix=this.namespace + "-flipSwitch-";//自定义id前缀

     /**
      * 组件主函数
      * @param param 组件调用时初始化参数集合
      */
    function tabs(param)
    {
        //定义组件创建以及事件处理主体对象
        var main={
                /**
                  * 组件版本号
                  */
                version:_version,

                /**
                  * 默认参数
                  */
                options:{
                    allowChange : true, 
                    beforeChange: function(){},//切换前回调函数
                    afterChange : function(){}//切换后回调函数
                },

                /**
                  * 储存初始化参数
                  */
                param:param,

                /**
                  * 缓存组件对象
                  */
                tabs:null,

                /**
                  * 回调函数集合
                  */
                callBacks:{},
              
                /**
                  * 初始化组件函数
                  */
                init:function() {
                   var _me = this;
                    
                   if (util.isString(this.param)) {
                        //作为  选择器  获取  input  集合
                        _me.tabs=$(this.param).find('input');
                        
                }
          
                   //初始化回调函数
                    this.initCallBack();

                    //绑定事件处理
                    this.bindEvent();

                },
                
                isAllowChange:function(element){
                   if(!element){
                        return false;
                    }
                    return true;
                },
                /**
                  * 初始化回调函数
                  */
                initCallBack:function(){

                    //初始化完成回调函数
                    this.bindCallBack("beforeChange",this.options.beforeChange);

                    //改变选中状态回调事件绑定
                    this.bindCallBack("afterChange",this.options.afterChange);

                },
                
                /**
                  * 绑定回调函数
                  * @param name 回调函数名称
                  * @param fn 回调函数
                  */
                bindCallBack:function(name,fn){
                    
                    //删除原回调函数
                    delete this.callBacks[name];

                    //如果fn参数类型为function则绑定
                    if(util.isFunction(fn)){
                        this.callBacks[name]=fn;
                    }
                },
   
               /**
                  * 执行回调函数
                  * @param name 回调函数名称
                  * @param context 执行回调函数的上下文(即改变执行函数中this指针)
                  * @param params 执行回调函数时传入的参数集合(数组)
                  * @returns 回调函数返回的结果
                  */
                runCallBack:function(name,context,params){
                    var result;
                    if (util.isFunction(this.callBacks[name])) {
                        if(context){
                           result = this.callBacks[name].apply(context,params);
                            
                        }else{
                            result=this.callBacks[name](params);
                        }
                    }
                    return result;
                },             
                
                /**
                 * 事件绑定
                 */
                bindEvent:function() {
                    var _me = this;
                    _me.tabs.each(function(index,item){
                        //console.log('item'+index);
                        $(item).off().on("click",function(){
                            var flag=_me.isAllowChange(this);
                            console.log(index);
                            // var index = index;
                            if(flag==true){
                                var status=true;
                            }else{
                                var status=false;
                            }
                                var result=_me.runCallBack("beforeChange",this,[status,index]);
    
                                //如果事件处理回调函数返回的值为false时阻止本次操作
                                if(util.isBoolean(result)&&!result){
                                    return false;
                                }
                                
                                 _me.runCallBack("afterChange",null,[status,index]);
                    });
                    
                    
                });
            }, 
                
                controlChange : function(val){
                    if(val){
                       $(main.tabs).removeAttr('disabled');
                    }else{
                        $(main.tabs).prop("disabled",!val);
                    }
                },
                changeTabs : function(index){
                    var _me = this ;
                    _me.tabs.removeAttr("checked");
                    $(_me.tabs[index-1]).prop("checked",true);
                    $($(_me.tabs).parent().parent().parent().find(".one_div")).hide();
                    $($(_me.tabs).parent().parent().parent().find(".one_div")[index-1]).show();
                }
         };           
               
        /**
         * 执行组件初始化
         */
        main.init();

        /**
          * 返回对外调用方法对象
          */
        return {
            /**
              * 设置是否允许设置选中状态
              * @param index 索引值
              * @param value bool值,true为允许,false为不允许
              */
            isAllowChoose : function(val){
                main.controlChange(val);
                return this;
            },
            changeTabsTo : function(index){
                main.changeTabs(index);
                return this;
            },
            /**
              * 改变状态之前回调函数
              * @param fn 组件改变状态之前回调函数
              */
            beforeChange:function(fn){
                main.bindCallBack("beforeChange",fn);
                return this;
            },
            
             afterChange:function(fn){
                main.bindCallBack("afterChange",fn);
                return this;
            },
            /**
              * 当前组件版本号
              */
            version : main.version
        }
   
}

    //扩展tabs组件
    exports.tabs = function (param) {
        if(util.isString(param)){
            
        return new tabs(param);
          
        }
        
        return null;
    };

}).call(one, Zepto);
/**
 * toast
 * @author 程伟凡
 * @version 1.0.1
 * required Zepto.js one.js
 * @modify kui.yang@newtouch.cn 阳葵
 * 1.修复部分语法错误
 * 2.增加永久显示的参数
 * 3.增加可供外部调用的方法用于关闭提示
 * 4.增加可以自定义显示的位置：left,top
 * 5.去除p标签，使用span标签
 */
;
(function ($) {

    this.ui = this.ui || {}; //定义ui对象。为避免覆盖如果存在ui对象则使用，不存在则新建
    var util = this.util, //用变量存储util.js中的方法，方便调用
        exports = this.ui, //用变量存储ui下的方法，可直接使用此变量追加组件。如不这样需要this.ui.add追加
        _version = "1.0.1",//组件版本号
        _id = 0,//id
        _catchPrefix = "ui-toast-",//组件缓存对象前缀
        _idPrefix = this.namespace + "-toast-";//自定义id前缀

    /**
     * 组件主函数
     * @param param 组件调用时初始化参数集合
     */
    function toast(param) {
        //定义组件创建以及事件处理主体对象
        var main = {
            /**
             * 组件版本号
             */
            version: _version,

            /**
             * 默认参数
             */
            options: {
                content: "",
                animate: true, //是否使用动画
                autoClose: 2000, //自动关闭时间,
                left:"20%",
                top:"75%"
            },

            /**
             * 储存初始化参数
             */
            param: param,

            /**
             * 缓存组件对象
             */
            toast: null,

            /*按钮结构模板*/
            _template: '<div class="one_toast" ><span></span></div>',

            /**
             * 初始化组件函数
             */
            init: function () {
                //把模板变为Zepto DOM对象
                this.toast = $(this._template);
                //把html追加到指定容器中
                $("body").append(this.toast);

                //object作为单个对象处理
                if (util.isObject(this.param)) {

                    //使用传入的参数覆盖默认参数
                    $.extend(this.options, this.param);

                }else if (util.isString(this.param)) {

                    main.setContent(this.param);

                }

                this.setAnimate(this.options.animate);//设置动画

                this.setContent(this.options.content);//设置内容

                //设置CSS样式
                util.setElementCss(this.toast, {
                    top: this.options.top,
                    left:this.options.left
                });

                return toast.param;
            },


            setAnimate: function (animate) { //设置动画
                var _me = this;
                if (animate) {
                    var i = 0;
                    var t = setInterval(function () {
                        i = i + 0.1;
                        if (i >= 1) {
                            clearInterval(t);
                        } else {
                            util.setElementCss(main.toast, {
                                opacity: i
                            });
                        }
                    }, 80);
                    setTimeout(function () {
                        var j = 1;
                        var ti = setInterval(function () {
                            j = j - 0.1;
                            if (j <= 0) {
                                clearInterval(ti);
                                _me.toast.remove();
                            } else {
                                util.setElementCss(main.toast, {
                                    opacity: j
                                });
                            }
                        }, 80);

                    }, main.options.autoClose);
                }else{
                    //直接设置成显示状态
                    util.setElementCss(main.toast, {
                        opacity: 1
                    });
                }

            },

            setContent: function (content) { //设置内容
                this.toast.find("span").eq(0).append(content);
            },
            /**
             * 隐藏组件
             */
            hide:function(){
                this.toast.remove();
            }
        };
        /**
         * 执行组件初始化
         */
        main.init();

        /**
         * 返回对外调用方法对象
         */
        return {

            openCallBack: function (fn) {
                main.options.openBack = fn;
                return this;
            },
            closeCallBack: function (fn) {
                main.options.closeBack = fn;
                return this;
            },
            hide:function(){
              main.hide();
            },
            /**
             * 当前组件版本号
             */
            version: main.version
        };
    }

    //扩展toast组件
    exports.toast = function (param) {
        //如果传入的是string类型,作为content内容
        if (!util.isString(param) && !util.isObject(param)) {
            return null;
        } else {
            return new toast(param);
        }
    };

}).call(one, Zepto);
