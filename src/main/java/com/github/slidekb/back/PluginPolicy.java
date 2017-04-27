/**
 Copyright 2017 John Kester (Jack Kester)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.github.slidekb.back;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class PluginPolicy extends Policy {

    public PermissionCollection getPermissions(CodeSource codeSource) {
        Permissions p = new Permissions();
        if (!codeSource.getLocation().toString().endsWith("Authorized.jar")) {
            p.add(new AllPermission());
        }
        return p;
    }


    public void refresh() {
    }

}
