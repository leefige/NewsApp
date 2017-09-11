# News App
An Android news App for Java project.

在需要调用service的地方添加
        Intent intent = new Intent(this, NewsService.class);
        String key = NewsService.KEY;
        String value = NewsService.LIST;
        intent.putExtra(key, value);
        startService(intent);

key固定为getBy，value为所需要的请求，如需要向service传递参数，则继续向intent里添加参数即可
key值可以直接调用NewsService.KEY得到，value值也从NewsService里的静态变量里找一下，如果没有直接在NewsService添加你们需要的静态字符串就好，我也好知道都需要哪些操作

在需要得到service的返回值的Activity里添加
private MyReceiver receiver = null;

在oncreate里添加
 //开启receiver,选择filter
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        //filter添加所想要接收的信号的名称，规范为android.intent.action. + 名称
        filter.addAction("NewsService.MAINACTION");
        //绑定filter
        MainActivity.this.registerReceiver(receiver,filter);
        //将MainActivity替换为你所添加的类的类名

filter所要添加的action可以跟key和value一样从NewsService里找，如果没有则自行在service里添加，只要不重复就好

在此Activity类里最后加上

 //receiver需要类
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            //TODO 你想进行的操作，由于数据只能从这里得到，所以要想进行使用需要在Activity里新增成员变量接收得到的数据，即可在方法内部实现数据调用
            //返回的数据的key也在NewsService的静态变量里有，如果没有请自行在service里添加
        }
    }

//若按照此方法接收service返回值则一个类只会收到一种返回值，若需要在一个activity里接收多个不同的service的返回值，则可以不绑定filter，在onReceive方法里对action信息进行判断手动执行不同的数据操作
