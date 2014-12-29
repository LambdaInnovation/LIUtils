#include <cstdio>
#include <string>

using namespace std;

const char *events[] = {
	"RenderTick",
	"ClientTick",
	"ServerTick",
	"WorldTick",
	"PlayerTick",
	"MouseInput",
	"KeyInput",
	"",
};

int main() {
	for (int i = 0; *events[i]; ++i) {
		const char *e = events[i];
		printf("public final HashSet<LIIHandler> set%s = new HashSet<LIIHandler>();\n\n", e);
		printf("@SubscribeEvent\n");
		printf("void on%s(%sEvent event) {\n", e, e);
		printf("\tfor (LIIHandler handler : set%s)\n", e);
		printf("\t\thandler.onEvent(event);\n");
		printf("}\n\n");
	}
	return 0;
}

