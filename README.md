# Compat Manager
A simple library to manage compatibility with other mods easily!

<img src="https://substackcdn.com/image/fetch/$s_!YdHr!,w_1456,c_limit,f_webp,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F5ad806d6-71b3-4d52-8e3b-0bd5bd816bc6_400x535.jpeg" width="200" alt="Let's just say I know a guy... who knows a guy... who knows another guy.">
<br>
When you create a compatibility with your mod with another, it's always a mess, and need to create classes that point to other classes to trick the classloader and let it not load 

That's where this library helps!

## Implementation
```groovy
repositories {
    maven {}
}
```

```groovy
dependencies {
    implementation("net.liukrast:compat_manager-1.21.1:1.0.1")
}
```

## Usage
1. Create a class
2. Annotate it wih `@Compat` and specify the mod you want to compat with
3. Create a constructor. The constructor will only init if the compat mod ID is found


