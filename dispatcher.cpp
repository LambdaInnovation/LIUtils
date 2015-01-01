#include <cstdio>

using namespace std;

const char *events[] = {
	"MouseInput",
	"KeyInput",
	"ItemPickup",
	"ItemCrafted",
	"ItemSmelted",
	"PlayerLoggedIn",
	"PlayerLoggedOut",
	"PlayerRespawn",
	"PlayerChangedDimension",
	"ServerTick",
	"ClientTick",
	"WorldTick",
	"PlayerTick",
	"RenderTick",
	""};

int main() {
	freopen("dispatcher.txt", "w", stdout);
	
	for (int i = 0; *events[i]; ++i) {
		const char *e = events[i];
		printf("public void register%s(LIHandler handler) {\n", e);
		printf("\tadd%s.add(handler);\n", e);
		printf("}\n\n");
	}
	
	for (int i = 0; *events[i]; ++i) {
		const char *e = events[i];
		printf("private final LinkedList<LIHandler> h%s = new LinkedList<LIHandler>();\n", e);
		printf("private final ArrayList<LIHandler> add%s = new ArrayList<LIHandler>();\n", e);
	}
	
	printf("\n");
	
	for (int i = 0; *events[i]; ++i) {
		const char *e = events[i];
		printf("@SubscribeEvent\n");
		printf("public void on%s(%sEvent event) {\n", e, e);
		printf("\tif (!add%s.isEmpty()) {\n", e);
		printf("\t\th%s.addAll(add%s);\n", e, e);
		printf("\t\tadd%s.clear();\n", e);
		printf("\t}\n");
		printf("\tfor (Iterator<LIHandler> it = h%s.iterator(); it.hasNext(); ) {\n", e);
		printf("\t\tLIHandler handler = it.next();\n");
		printf("\t\tif (handler.isDead())\n");
		printf("\t\t\tit.remove();\n");
		printf("\t\telse\n");
		printf("\t\t\thandler.trigger(event);\n");
		printf("\t}\n");
		printf("}\n\n");
	}
	
	printf("@SubscribeEvent\n");
	printf("public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) {\n");
	for (int i = 0; *events[i]; ++i) {
		const char *e = events[i];
		printf("\th%s.clear();\n", e);
		printf("\tadd%s.clear();\n", e);
	}
	printf("}\n");
	
	return 0;
}

