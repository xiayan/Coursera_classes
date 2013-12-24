// 2.08, 2.9, 2.18

#include <iostream>

using namespace std;
const int item = 5;

float p_constant(float *, int, int, int);
void printmatrix(float a[][5], int);
void p_matrix(float **, int);

int main(int argc, const char *argv[]) {
    //float data[] = {0.0, 0.05, 0.4, 0.08, 0.04, 0.1, 0.1, 0.23};
    // float data[] = {0.03, 0.05, 0.17, 0.1, 0.2, 0.2, 0.25};
    float data[] = {0.25, 0.1, 0.2, 0.3, 0.15};
    //float result[8][8];
    float result[5][5];
    float **rt = new float *[item];
    
    for (int i = 0; i < item; i++)
        rt[i] = new float[item];

    
    for (int i = 0; i < item; i++)
        for (int j = 0; j < item; j++) {
            result[i][j] = 0.0;
            rt[i][j] = 0.0;
    }

    for (int i = 0; i < item; i++) {
        result[0][i] = data[i];
        rt[0][i] = data[i];
    }

    for (int s = 0; s < item - 1; s++) {
        for (int i = 1; i < item; i++) {
            if (i + s >= item) continue;
            float cst = p_constant(data, item, i, s);
            float min = 99999999.0;
            float p_min = 99999999.0;
            for (int r = i; r <= i + s; r++) {
                float fi = (i > r - 1) ? 0.0 : result[i][r - 1];
                float se = (r + 1 > i + s) ? 0.0 : result[r + 1][i + s];
                float local = fi + se;
                if (local < min)
                    min = local;
                float p_fi = (i > r - 1) ? 0.0 : rt[i][r - 1];
                float p_se = (r + 1 > i + s) ? 0.0 : rt[r + 1][i + s];
                float p_local = p_fi + p_se;
                if (p_local < p_min)
                    p_min = p_local;
                /*
                if (s == 6 && i == 1) {
                    cout << i << " " << (i + s) << " " << r << endl;
                    cout << fi << " " << se << endl;
                    cout << min << " " << cst << endl;
                }
                */
            }
            result[i][i+s] = min + cst;
            rt[i][i+s] = p_min + cst;
        }
    }

    //printmatrix(result, item);
    p_matrix(rt, item);
    cout << rt[1][item - 1] << endl;

    for (int i = 0; i < item; i++)
        delete[] rt[i];

    delete[] rt;

    return 0;
}

float p_constant(float *a, int l, int i, int s) {
    float result = 0;
    for (int m = i; m <= i + s; m++)
        result += a[m];

    return result;
}

void printmatrix(float a[][5], int l) {
    for (int i = 0; i < l; i++) {
        for (int j = 0; j < 5; j++) {
            cout << a[i][j] << "    ";
        }
        cout << endl;
    }
}

void p_matrix(float **a, int l) {
    for (int i = 0; i < l; i++) {
        for (int j = 0; j < 5; j++) {
            cout << a[i][j] << "    ";
        }
        cout << endl;
    }
}
