# News App
An Android news App for Java project.

在需要调用service的地方添加
        Intent intent = new Intent(this, NewsService.class);
        String key = "getBy";
        String value = "List";
        intent.putExtra(key, value);
        startService(intent);

key固定为getBy，value为所需要的请求，如需要向service传递参数，则继续向intent里添加参数即可

在需要得到service的返回值的Activity里添加
private MyReceiver receiver = null;

在oncreate里添加
 //开启receiver,选择filter
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        //filter添加所想要接收的信号的名称，规范为android.intent.action. + 名称
        filter.addAction("android.intent.action.MY_BROADCAST");
        //绑定filter
        MainActivity.this.registerReceiver(receiver,filter);
        //将MainActivity替换为你所添加的类的类名

在此Activity类里最后加上

 //receiver需要类
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            //TODO 你想进行的操作，由于数据只能从这里得到，所以要想进行使用需要在Activity里新增成员变量接收得到的数据，即可在方法内部实现数据调用
        }
    }

//若按照此方法接收service返回值则一个类只会收到一种返回值，若需要在一个activity里接收多个不同的service的返回值，则可以不绑定filter，在onReceive方法里对action信息进行判断手动执行不同的数据操作
