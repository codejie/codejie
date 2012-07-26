
#include <iostream>


	//private static final float rateTable[][] = {
	//		{ 1.75f, 0.80f, 0.45f, 0.17f },
	//		{ 1.50f, 1.25f, 0.55f, 0.20f },
	//		{ 1.00f, 0.80f, 0.45f, 0.20f },
	//		{ 0.80f, 0.50f, 0.30f, 0.17f }
	//};
	//
	//private static final int judgeTable[][] = {
	//	{ 0, 1, 1, 2 },
	//	{ 2, 2, 3, 3 } 
	//};


	//	int check = judgeTable[judge][score];
	//	
	//	Log.d(Global.APP_TITLE, data.word + " check : " + check);

	//	data.next = (data.last != 0) ? deltaUpdated - data.last : UPDATED_START;
	//	data.next *= rateTable[data.score][check];
	//	data.next += (deltaUpdated + 1);

float rateTable[4][4] = {
		{ 1.75f, 0.80f, 0.45f, 0.17f },
		{ 1.50f, 1.25f, 0.55f, 0.20f },
		{ 1.00f, 0.80f, 0.45f, 0.20f },
		{ 0.80f, 0.50f, 0.30f, 0.17f }
};

int judgeTable[2][4] = {
	{ 0, 1, 1, 2 },
	{ 2, 2, 3, 3 } 
};

int test()
{
	int preScore = 3; //0 ~ 3
	int judge = 0;//yes, no
	int curScore = 0;


	int last = 0;
	int next = 0;
	int updated = 0;
	int check = 0;//judgeTable[judge][preScore];
	int num = 0;
	while(++ num < 10)
	{
		judge = (judge == 0) ? 1 : 0;
		check = judgeTable[judge][preScore];
		next = ((last != 0) ? (updated - last) : 7) * rateTable[curScore][check] + updated + 1;
		std::cout << updated << "," << next << "," << (next - updated) << std::endl;
		last = updated;
		updated = next;
		preScore = curScore;
		if(curScore < 4)
			++ curScore;
	}
	
	return 0;
}

int main()
{
	test();
	return 0;
}