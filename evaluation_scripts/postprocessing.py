# from nltk.tokenize import word_tokenize
# from nltk.translate.bleu_score import sentence_bleu
# from nltk.translate.bleu_score import SmoothingFunction
# import scipy.stats as stats
# from cliffs_delta import cliffs_delta
from glob import glob

class ExperimentResult:
    """ Class to hold the output of a single experiment for easier managmenet """
    def __init__(self, references, predictions, stats, loss) -> None:
        self.references = references
        self.predictions = predictions
        self.stats = stats
        self.loss = loss
 
# Global variable to hold the data for each experiment
experiment_data = []

def main():
    """ Main program """
    # Load the data by creating an ExperimentResults object for each experiment
    loadData()

    # Process the gathered data.
    # run_statistical_tests()
    # generate_graphs()

    return 0

def loadData():

    for dir in glob("../models/exp_*/"):
        references = open(dir + "ref.txt").readlines()
        predictions = open(dir + "pred_com.txt").readlines()

        # the format of stats is (separated by space):
        # epoch, accuracy, precision, recall, f1 
        stats = open("stats.txt").readlines()
        # the format of loss is:
        # batch number, average loss, throughput
        loss = open("loss.txt").readlines()
        res = ExperimentResult(references, predictions, stats, loss)

        experiment_data.append(res)

def run_statistical_tests():

    # TODO: For Leonhard: this is how I calculated statistics for my results. I leave it as a reference. - Balys.

    # for i, ref in enumerate(references):
    #     ref_tokens = set(word_tokenize(ref))
    #     com_pred_tokens = set(word_tokenize(predictions_com[i]))
    #     no_com_pred_tokens = set(word_tokenize(predictions_no_com[i]))
    #
    #     # Jaccard distance
    #     jac = 1 - (len(ref_tokens & com_pred_tokens) / len(ref_tokens | com_pred_tokens))
    #     jac_no = 1 - (
    #         len(ref_tokens & no_com_pred_tokens) / len(ref_tokens | no_com_pred_tokens)
    #     )
    #     com_jac.append(jac)
    #     no_com_jac.append(jac_no)
    #
    #     # BLEU score
    #     bleu = sentence_bleu(
    #         ref_tokens, com_pred_tokens, smoothing_function=SmoothingFunction().method1
    #     )
    #     bleu_no = sentence_bleu(
    #         ref_tokens, no_com_pred_tokens, smoothing_function=SmoothingFunction().method1
    #     )
    #     com_bleu.append(bleu)
    #     no_com_bleu.append(bleu_no)
    #
    # print(
    #     "Rank Sum with BLEU:\n", stats.ranksums(com_bleu, no_com_bleu, alternative="less")
    # )
    # print(
    #     "Rank Sum  with Jaccard Distance:\n",
    #     stats.ranksums(com_jac, no_com_jac, alternative="less"),
    # )
    #
    # print("BLEU Cliff Delta:", cliffs_delta(com_bleu, no_com_bleu))
    # print("Jaccard Cliff Delta:", cliffs_delta(com_jac, no_com_jac))

    pass
