/*
 * TasksListViewActivity.java
 *
 * Copyright (c) 2011  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */

package com.softenido.examples;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.softenido.cafecore.util.Strings;
import com.softenido.droiddesk.admob.AdMob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksListViewActivity extends ListActivity
{
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        admob = AdMob.addBanner(this, true);

        load(R.id.tasks_menu_app);
    }
    void load(int type)
    {
        String title;
        int count =0;
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        PackageManager pkgManager= (PackageManager) this.getPackageManager();


        final List<String> names = new ArrayList<String>();
        final List<String> texts = new ArrayList<String>();

        switch (type)
        {
            case R.id.tasks_menu_app:
            {
                title = "App List";
                List<ActivityManager.RunningAppProcessInfo> app = manager.getRunningAppProcesses();
                for(int i =0;i<app.size();i++)
                {
                    count++;
                    ActivityManager.RunningAppProcessInfo item = app.get(i);
                    names.add(item.processName);

                    String txt = "name: "+item.processName;
                    for(int j=0;j<item.pkgList.length;j++)
                    {
                        txt += "\npkg["+j+"]: "+item.pkgList[j];
                    }
                    texts.add(txt);
                }
            }
            break;
            case R.id.tasks_menu_srv:
            {
                title = "Service List";
                List<ActivityManager.RunningServiceInfo> srv = manager.getRunningServices(1000);
                for(int i =0;i<srv.size();i++)
                {
                    count++;
                    ActivityManager.RunningServiceInfo item = srv.get(i);
                    names.add(item.process);

                    String txt = "name: "+item.process+"\n"+
                                 "clientPackage: "+item.clientPackage;
                    texts.add(txt);
                }
            }
            break;
            case R.id.tasks_menu_task:
            {
                title = "Tasks List";
                List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1000);
                for(int i =0;i<tasks.size();i++)
                {
                    count++;
                    ActivityManager.RunningTaskInfo item = tasks.get(i);
                    names.add(item.baseActivity.toShortString());

                    String txt = "name: "+item.baseActivity;
                    if(item.description!=null)
                        txt += "\ndescription: %s " +item.description.toString();
                    if(item.topActivity!=null)
                        txt += "\ntop: "+item.topActivity.toShortString();
                    texts.add(txt);
                }
            }
            case R.id.tasks_menu_pkg:
            {
                title = "Package List";
                List <PackageInfo> pkgs=pkgManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
                for(int i =0;i<pkgs.size();i++)
                {
                    count++;
                    PackageInfo item = pkgs.get(i);

                    names.add(item.packageName);

                    String txt = "permissions: {";
                    if(item.permissions==null)
                    {
                        txt += "null";
                    }
                    else
                    {
                        for( PermissionInfo pi : item.permissions)
                        {
                            txt += "[group="+pi.group+", level="+pi.protectionLevel+", name"+pi.toString()+"]";
                        }
                    }
                    txt+="}";
                    texts.add(txt);
                }
            }
            case R.id.tasks_menu_perm:
            {
                title = "Permissions List";
                List <PackageInfo> pkgs=pkgManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

                Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
                for(int i =0;i<pkgs.size();i++)
                {
                    PackageInfo item = pkgs.get(i);
                    
                    if(item.permissions==null)
                        continue;
                    for( PermissionInfo pi : item.permissions)
                    {
                        ArrayList<String> list = map.get(pi.toString());
                        if(list==null)
                        {
                            list = new ArrayList<String>();
                            map.put(pi.toString(),list);
                        }
                        list.add(item.packageName);
                    }
                }
                for(Map.Entry<String, ArrayList<String>> e : map.entrySet())
                {
                    String txt = "Packages:{";
                    Strings.commaSeparatedValues(e.getValue());
                    txt +="}";
                    names.add(e.getKey());
                    texts.add(txt);
                }
            }
            break;
            default:
                return;
        }
        setTitle(title+" ("+count+")");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.tasks_listview_item, R.id.editText, names);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), texts.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), texts.get(position), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tasks_listview_menu,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        load(item.getItemId());
        return true;
    }
}
