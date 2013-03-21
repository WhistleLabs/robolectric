package org.robolectric;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class EnvHolder {
    public final Map<File, AndroidManifest> appManifestsByFile = new HashMap<File, AndroidManifest>();
    public final Map<SdkConfig, SoftReference<SdkEnvironment>> sdkToEnvironmentSoft = new HashMap<SdkConfig, SoftReference<SdkEnvironment>>();
    public final Map<SdkConfig, SdkEnvironment> sdkToEnvironment = new HashMap<SdkConfig, SdkEnvironment>();
}
